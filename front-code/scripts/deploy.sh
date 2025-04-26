#!/bin/bash

# Exit on error
set -e

echo "Starting deployment process..."

# Install dependencies
echo "Installing dependencies..."
npm install

# Build for production
echo "Building for production..."
npm run build

# Output success message
echo "Build completed successfully!"
echo "The application is ready to be deployed from the 'dist' directory."

# Instructions for serving the built app
echo ""
echo "To serve the built application with a simple HTTP server, run:"
echo "  npm install -g serve"
echo "  serve -s dist"
echo ""
echo "Or copy the files from the 'dist' directory to your web server." 