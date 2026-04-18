$root = "c:\Users\alexa\OneDrive\Documentos\NeriTech\backend\src\main\java"
$files = Get-ChildItem -Path $root -Recurse -Filter "*.java"

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    
    # Check if uses Lombok annotations that imply getters/setters
    if ($content -match "@Data" -or $content -match "@Getter" -or $content -match "@Setter") {
        
        # Regex to find private fields: private Type name;
        # Ignora campos static ou final (final requer construtor, não setter, mas getter sim. Vamos simplificar e ignorar final por enquanto para setters)
        $fields = [regex]::Matches($content, "private\s+(?!static)([\w<>?\[\]\.]+)\s+(\w+);")
        
        $methodsToAdd = ""
        
        foreach ($match in $fields) {
            $type = $match.Groups[1].Value
            $name = $match.Groups[2].Value
            $capitalizedName = $name.Substring(0,1).ToUpper() + $name.Substring(1)
            
            # Getter
            $getterName = "get$capitalizedName"
            if ($type -eq "boolean" -or $type -eq "Boolean") {
                if ($name -match "^is[A-Z]") {
                   $getterName = $name
                } else {
                   $getterName = "is$capitalizedName"
                }
            }
            
            # Check if getter exists
            if (-not ($content -match "$getterName\s*\(")) {
                $methodsToAdd += "`n    public $type $getterName() {`n        return this.$name;`n    }"
            }
            
            # Setter (skip if final)
            if (-not ($content -match "final\s+.*$name")) {
                $setterName = "set$capitalizedName"
                # Check if setter exists
                if (-not ($content -match "$setterName\s*\(")) {
                    $methodsToAdd += "`n    public void $setterName($type $name) {`n        this.$name = $name;`n    }"
                }
            }
        }
        
        if ($methodsToAdd -ne "") {
            # Insert before last brace
            $lastBraceIndex = $content.LastIndexOf("}")
            if ($lastBraceIndex -gt 0) {
                $newContent = $content.Substring(0, $lastBraceIndex) + $methodsToAdd + "`n}"
                Set-Content -Path $file.FullName -Value $newContent -Encoding UTF8
                Write-Host "Added methods to $($file.Name)"
            }
        }
    }
}
