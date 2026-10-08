$ErrorActionPreference = 'Stop'
$version = '3.9.11'
$root = Join-Path $env:USERPROFILE ".m2\wrapper\dists\sgac-maven-$version"
$bin = Join-Path $root "apache-maven-$version\bin\mvn.cmd"
if (Test-Path $bin) { exit 0 }
New-Item -ItemType Directory -Force -Path $root | Out-Null
$zip = Join-Path $root "apache-maven-$version-bin.zip"
$url = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$version/apache-maven-$version-bin.zip"
try {
    Write-Host "Baixando Maven $version (primeira execucao)..."
    Invoke-WebRequest -Uri $url -OutFile $zip
    Expand-Archive -Path $zip -DestinationPath $root -Force
    Remove-Item $zip -Force
    if (-not (Test-Path $bin)) { throw 'Maven nao foi extraido corretamente.' }
} catch {
    Write-Error $_
    exit 1
}
