rem Assumes CHANGELOG_GITHUB_TOKEN environment variable has been set
set SSL_CERT_FILE=C:\RailsInstaller\cacert.pem
github_changelog_generator -o ..\..\CHANGELOG.md
