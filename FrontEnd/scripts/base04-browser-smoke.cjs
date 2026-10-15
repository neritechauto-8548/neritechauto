// BASE-04: real compiled Angular login smoke in Chrome, no new libraries.
// Chrome DevTools Protocol is accessed via Node 22's built-in WebSocket.
const { test } = require('node:test');
const assert = require('node:assert/strict');
const { spawn, execFileSync } = require('node:child_process');
const { createServer } = require('node:http');
const { readFileSync, existsSync, mkdtempSync, rmSync, mkdirSync, writeFileSync } = require('node:fs');
const { resolve, extname, sep } = require('node:path');
const { tmpdir } = require('node:os');
const dist = resolve(__dirname, '../dist/ng-matero/browser');
const screenshots = resolve(__dirname, '../test-results/base04');
const sleep = ms => new Promise(done => setTimeout(done, ms));
const mime = { '.js':'text/javascript', '.css':'text/css', '.svg':'image/svg+xml',
  '.json':'application/json', '.png':'image/png', '.woff2':'font/woff2', '.html':'text/html' };
let server, chrome, profile, ws, msgId = 0, baseUrl;
const pending = new Map();

function chromeBinary() {
  for (const binary of [process.env.CHROME_BIN, 'google-chrome', 'google-chrome-stable', 'chromium'].filter(Boolean)) {
    try { execFileSync(binary, ['--version'], { stdio: 'ignore' }); return binary; } catch {}
  }
  throw Error('Chrome is not installed on the CI runner');
}
async function startServer() {
  server = createServer((req, res) => {
    const name = new URL(req.url, 'http://localhost').pathname;
    const path = resolve(dist, '.' + decodeURIComponent(name));
    const filename = path.startsWith(dist + sep) && existsSync(path) ? path : resolve(dist, 'index.html');
    try {
      const body = readFileSync(filename);
      res.writeHead(200, { 'Content-Type': mime[extname(filename)] || 'application/octet-stream' });
      res.end(body);
    } catch { res.writeHead(404); res.end('Not found'); }
  });
  await new Promise(done => server.listen(0, '127.0.0.1', done));
  baseUrl = 'http://127.0.0.1:' + server.address().port;
}
async function startBrowser() {
  profile = mkdtempSync(resolve(tmpdir(), 'neritech-cdp-'));
  chrome = spawn(chromeBinary(), [
    '--headless=new', '--no-sandbox', '--disable-dev-shm-usage', '--no-first-run',
    '--disable-background-networking', '--remote-debugging-port=0',
    '--remote-allow-origins=*', '--user-data-dir=' + profile, 'about:blank',
  ], { stdio: 'ignore' });
  let port = 0;
  for (let i = 0; i < 160; i++) {
    const file = resolve(profile, 'DevToolsActivePort');
    if (existsSync(file)) { port = Number(readFileSync(file, 'utf8').split('\n')[0]); break; }
    if (chrome.exitCode !== null) throw Error('Chrome quit during startup');
    await sleep(100);
  }
  assert.ok(port, 'Chrome DevTools port missing');
  const targets = await (await fetch('http://127.0.0.1:' + port + '/json/list')).json();
  const page = targets.find(t => t.type === 'page');
  assert.ok(page?.webSocketDebuggerUrl, 'Chrome page target missing');
  ws = new WebSocket(page.webSocketDebuggerUrl);
  ws.addEventListener('message', event => {
    const data = JSON.parse(String(event.data));
    if (!pending.has(data.id)) return;
    const callback = pending.get(data.id);
    pending.delete(data.id);
    data.error ? callback.reject(Error(JSON.stringify(data.error))) : callback.resolve(data.result);
  });
  await new Promise((done, reject) => {
    ws.addEventListener('open', done, { once: true });
    ws.addEventListener('error', reject, { once: true });
  });
  await command('Page.enable');
  await command('Runtime.enable');
}
function command(method, params = {}) {
  return new Promise((resolve, reject) => {
    const id = ++msgId;
    pending.set(id, { resolve, reject });
    ws.send(JSON.stringify({ id, method, params }));
    setTimeout(() => {
      if (pending.has(id)) { pending.delete(id); reject(Error('CDP timeout ' + method)); }
    }, 15000).unref();
  });
}
async function evaluate(expression) {
  const out = await command('Runtime.evaluate', { expression, returnByValue:true, awaitPromise:true });
  if (out.exceptionDetails) throw Error('Page exception ' + JSON.stringify(out.exceptionDetails).slice(0,300));
  return out.result.value;
}
async function waitFor(expression) {
  for (let i = 0; i < 90; i++) {
    if (await evaluate(expression)) return;
    await sleep(150);
  }
  throw Error('Angular login screen did not render');
}
async function setViewport(width) {
  await command('Emulation.setDeviceMetricsOverride',
    { width, height:900, deviceScaleFactor:1, mobile:false });
  await sleep(150);
}
async function screenshot(filename) {
  const image = await command('Page.captureScreenshot', { format:'png', captureBeyondViewport:false });
  mkdirSync(screenshots, { recursive:true });
  writeFileSync(resolve(screenshots, filename + '.png'), Buffer.from(image.data, 'base64'));
}
async function cleanup() {
  try { ws?.close(); } catch {}
  try { chrome?.kill('SIGKILL'); } catch {}
  if (server) await new Promise(done => server.close(done));
  try { if (profile) rmSync(profile, { recursive:true, force:true }); } catch {}
}

test('BASE-04: responsive login, focus and simulated 200% zoom in real Chrome',
  { timeout: 110000 }, async () => {
  assert.ok(existsSync(resolve(dist,'index.html')), 'Run yarn build first');
  try {
    await startServer();
    await startBrowser();
    await command('Page.navigate', { url:baseUrl + '/auth/login' });
    await waitFor("!!document.querySelector('#login-title') && !!document.querySelector('#email') && !!document.querySelector('#password')");
    for (const width of [320,768,1440]) {
      await setViewport(width);
      const result = await evaluate("(() => { const form = document.querySelector('.auth-form'); const r = form.getBoundingClientRect(); return { width:innerWidth, pageOverflow:document.documentElement.scrollWidth-innerWidth, formRight:r.right, formWidth:r.width }; })()");
      assert.equal(result.width, width, 'viewport width');
      assert.ok(result.formWidth > 0 && result.formRight <= width+2 && result.pageOverflow <= 2,
        'Horizontal overflow at width ' + width + ': ' + JSON.stringify(result));
      await screenshot('login-' + width);
    }
    await setViewport(1440);
    // The browser accessibility tree is inspectable without installing a screen
    // reader. This verifies semantics, NOT NVDA/VoiceOver speech output.
    const ax = await command('Accessibility.getFullAXTree');
    const accessible = ax.nodes
      .filter(node => node.role?.value && node.name?.value)
      .map(node => ({ role: node.role.value, name: node.name.value }));
    assert.ok(accessible.some(item => item.role === 'heading' && item.name.includes('Entrar no NERITECH')),
      'Login heading absent from Chrome accessibility tree');
    assert.ok(accessible.some(item => item.role === 'textbox' && item.name.includes('E-mail')),
      'Email input must have an accessible name');
    assert.ok(accessible.some(item => item.role === 'textbox' && item.name.includes('Senha')),
      'Password input must have an accessible name');
    assert.ok(accessible.some(item => item.role === 'button' && item.name === 'Entrar'),
      'Submit button must have an accessible name');
    assert.equal(await evaluate("(() => { document.querySelector('#email').focus(); return document.activeElement.id; })()"), 'email');
    await command('Input.dispatchKeyEvent',{type:'keyDown',key:'Tab',code:'Tab',windowsVirtualKeyCode:9});
    await command('Input.dispatchKeyEvent',{type:'keyUp',key:'Tab',code:'Tab',windowsVirtualKeyCode:9});
    const tab = await evaluate("(() => ({tag:document.activeElement.tagName, visible:document.activeElement.matches(':focus-visible')}))()");
    assert.ok(tab.visible && ['A','BUTTON','INPUT'].includes(tab.tag),
      'Keyboard Tab should move to a focused control: ' + JSON.stringify(tab));
    await setViewport(768);
    const zoom = await evaluate("(() => { document.documentElement.style.zoom='200%'; const r=document.querySelector('.auth-form').getBoundingClientRect(); return {width:innerWidth, right:r.right, overflow:document.documentElement.scrollWidth-innerWidth}; })()");
    assert.ok(zoom.right <= zoom.width+2 && zoom.overflow <= 2,
      'CSS 200% zoom reflow overflows: ' + JSON.stringify(zoom));
    await screenshot('login-css-zoom-200');

    // AUTH-002: inspect the compiled recovery page, not a mock HTML specimen.
    await command('Page.navigate', { url: baseUrl + '/auth/recover' });
    await waitFor("!!document.querySelector('#recover-title') && !!document.querySelector('#recover-email')");
    for (const width of [320, 768, 1440]) {
      await setViewport(width);
      const layout = await evaluate("(() => { const form = document.querySelector('.auth-form'); const rect = form.getBoundingClientRect(); const back = document.querySelector('a.back-link'); return { viewport: innerWidth, overflow: document.documentElement.scrollWidth - innerWidth, formRight: rect.right, formWidth: rect.width, backLink: back?.getAttribute('href') }; })()");
      assert.ok(layout.formWidth > 0 && layout.formRight <= width + 2 && layout.overflow <= 2,
        'AUTH-002 overflow at ' + width + ': ' + JSON.stringify(layout));
      assert.equal(layout.backLink, '/auth/login');
      await screenshot('recover-' + width);
    }
    const recoveryAX = await command('Accessibility.getFullAXTree');
    const recoveryNames = recoveryAX.nodes.filter(n => n.role?.value && n.name?.value)
      .map(n => ({role:n.role.value, name:n.name.value}));
    assert.ok(recoveryNames.some(n => n.role === 'heading' && n.name.includes('Recuperar acesso')),
      'AUTH-002 heading not available to assistive technologies');
    assert.ok(recoveryNames.some(n => n.role === 'textbox' && n.name.includes('E-mail')),
      'AUTH-002 input missing accessible E-mail label');
    assert.ok(recoveryNames.some(n => n.role === 'link' && n.name.includes('Voltar para o login')),
      'AUTH-002 return link missing accessible name');
    await setViewport(768);
    const recoverZoom = await evaluate("(() => { document.documentElement.style.zoom='200%'; const r=document.querySelector('.auth-form').getBoundingClientRect(); return { width: innerWidth, right:r.right, overflow:document.documentElement.scrollWidth-innerWidth }; })()");
    assert.ok(recoverZoom.right <= recoverZoom.width+2 && recoverZoom.overflow <= 2,
      'AUTH-002 CSS zoom reflow: ' + JSON.stringify(recoverZoom));
    await screenshot('recover-css-zoom-200');

  } finally { await cleanup(); }
});
// CSS zoom approximates reflow only. OS/browser zoom, screen reader, authentication
// and a real online/offline backend remain separate manual acceptance gates.
