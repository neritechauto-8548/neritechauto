const fs = require('fs');
let f = 'src/app/routes/configuracoes/colaboradores/cadastro-colaborador/cadastro-colaborador.html';
let c = fs.readFileSync(f, 'utf8');

let blockRegex = /(<!-- Tipo de Estilo \(Preset\) -->.*?<!-- Tema da Barra Superior -->.*?<\/p-panel>\s*)/s;
let match = c.match(blockRegex);

if (match) {
    c = c.replace(match[0], '');
    c = c.replace(/<\/ng-container>\s*<\/ng-container>\s*<\/div>\s*<\/div>\s*$/, '\n' + match[0] + '\n      </ng-container>\n    </ng-container>\n\n  </div>\n</div>\n');
    fs.writeFileSync(f, c);
    console.log('Fixed position');
} else {
    console.log('Not found');
}
