const { chromium } = require('C:/Users/alexa/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
const dir = 'C:/Users/alexa/.codex/visualizations/2026/10/25/01a17353-3351-7140-8b28-9e5bd50a146f';
(async () => {
  const browser = await chromium.launch({executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe',headless:true,args:['--no-sandbox']});
  for (const width of [1440,768,375,320]) {
    const page = await browser.newPage({viewport:{width,height:900},deviceScaleFactor:1});
    await page.goto('http://127.0.0.1:4200/',{waitUntil:'domcontentloaded'});
    await page.waitForTimeout(800);
    const data = await page.evaluate(() => ({h1:document.querySelector('h1')?.innerText, overflow:document.documentElement.scrollWidth > innerWidth, width:document.documentElement.scrollWidth, mosaic:document.querySelectorAll('.home-mosaic>.mosaic-card').length, faqs:document.querySelectorAll('.faq-list details').length, integrationNodes:document.querySelectorAll('.integration-orbit article').length, order:[...document.querySelectorAll('main section')].map(el=>el.className.split(' ')[0])}));
    console.log(width,JSON.stringify(data));
    await page.screenshot({path:`${dir}/fidelity-hero-${width}.png`});
    if (width === 1440) {
      for (const name of ['evolution-band','home-mosaic-section','journey-section','photo-break','operations-band','orbit-section','brand-quote','faq']) {
        await page.locator('.'+name).screenshot({path:`${dir}/fidelity-${name}.png`,timeout:30000});
      }
    }
    if (width === 375) await page.locator('.home-mosaic-section').screenshot({path:`${dir}/fidelity-mobile-mosaic.png`,timeout:30000});
    await page.close();
  }
  await browser.close();
})().catch(e=>{console.error(e);process.exit(1)});
