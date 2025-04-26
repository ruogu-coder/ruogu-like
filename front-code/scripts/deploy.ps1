# PowerShell deployment script

Write-Host "Starting deployment process..." -ForegroundColor Green

# Install dependencies
Write-Host "Installing dependencies..." -ForegroundColor Cyan
npm install

# Build for production
Write-Host "Building for production..." -ForegroundColor Cyan
npm run build

# Output success message
Write-Host "Build completed successfully!" -ForegroundColor Green
Write-Host "The application is ready to be deployed from the 'dist' directory."

# Instructions for serving the built app
Write-Host ""
Write-Host "To serve the built application with a simple HTTP server, run:" -ForegroundColor Yellow
Write-Host "  npm install -g serve" -ForegroundColor Gray
Write-Host "  serve -s dist" -ForegroundColor Gray
Write-Host ""
Write-Host "Or copy the files from the 'dist' directory to your web server." -ForegroundColor Yellow 