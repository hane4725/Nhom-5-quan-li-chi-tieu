Add-Type -AssemblyName System.IO.Compression.FileSystem
$originalPath = "C:\Users\Hoang Hai ^^\Downloads\Cauhoiontap.docx"
$tempDocx = "$env:TEMP\Cauhoiontap_copy_$(Get-Random).docx"
Copy-Item -Path $originalPath -Destination $tempDocx -Force

$extractPath = "$env:TEMP\docx_extract_$(Get-Random)"
[System.IO.Compression.ZipFile]::ExtractToDirectory($tempDocx, $extractPath)
$xml = [xml](Get-Content "$extractPath\word\document.xml")
$nsm = New-Object System.Xml.XmlNamespaceManager($xml.NameTable)
$nsm.AddNamespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main")

$paragraphs = $xml.SelectNodes("//w:p", $nsm)
foreach ($p in $paragraphs) {
    $texts = $p.SelectNodes(".//w:t", $nsm)
    $pText = ""
    foreach ($t in $texts) { $pText += $t.InnerText }
    if ($pText.Trim() -ne "") { Write-Host $pText }
}
Remove-Item -Recurse -Force $extractPath
Remove-Item -Force $tempDocx
