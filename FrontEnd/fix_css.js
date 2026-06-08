const fs = require('fs');
let f = 'src/app/routes/configuracoes/colaboradores/cadastro-colaborador/cadastro-colaborador.ts';
let c = fs.readFileSync(f, 'utf8');

// Modifica menuThemeOptions
c = c.replace(/value:\s*'bg-([^\s']+) text-([^\s']+)(.*?)'/g, "value: '!bg-$1 !text-$2$3'");

fs.writeFileSync(f, c);
console.log('Fixed CSS modifiers');
