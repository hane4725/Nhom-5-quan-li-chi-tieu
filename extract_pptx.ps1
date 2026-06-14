Add-Type -AssemblyName System.IO.Compression.FileSystem
$zipPath = "C:\Users\Hoang Hai ^^\Downloads\BudgetBuddy_Nhom5 (1).pptx"
$extractPath = "$env:TEMP\pptx_extract_$(Get-Random)"
[System.IO.Compression.ZipFile]::ExtractToDirectory($zipPath, $extractPath)

$slideFiles = Get-ChildItem -Path "$extractPath\ppt\slides" -Filter "slide*.xml" | Sort-Object { [int]($_ -replace '\D','') }

foreach ($file in $slideFiles) {
    Write-Host "`n--- Slide $($file.Name) ---"
    $xml = [xml](Get-Content $file.FullName)
    $nsm = New-Object System.Xml.XmlNamespaceManager($xml.NameTable)
    $nsm.AddNamespace("a", "http://schemas.openxmlformats.org/drawingml/2006/main")
    $texts = $xml.SelectNodes("//a:t", $nsm)
    if ($texts) {
        foreach ($t in $texts) { Write-Host $t.InnerText }
    }
}
Remove-Item -Recurse -Force $extractPath
