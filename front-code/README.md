# Billion-Level Like System Frontend

This is the frontend for the Billion-Level Like System, a high-performance application supporting billions of like operations with Redis, Message Queue, and TiDB.

## Features

- User management (register, login, profile, password change)
- Blog management (list, detail view)
- Realtime like/unlike functionality
- Admin panel for user management

## Tech Stack

- Vue.js 3
- Vite
- Ant Design Vue
- Pinia for state management
- Vue Router for routing
- Axios for API calls

## Prerequisites

- Node.js 16.x or higher
- NPM 8.x or higher

## Project Setup

1. Clone the repository:

```bash
git clone <repository-url>
cd ruogu-like/front-code
```

2. Install dependencies:

```bash
npm install
```

3. Start the development server:

```bash
npm run dev
```

The application will be available at: http://localhost:5173

## Build for Production

To build the application for production:

```bash
npm run build
```

The built files will be in the `dist` directory.

## API Integration

The frontend integrates with the backend API at `http://localhost:8080/api`. Make sure the backend server is running before starting the frontend application.

## Project Structure

- `src/api`: API request modules
- `src/components`: Reusable Vue components
- `src/layouts`: Layout components
- `src/router`: Vue Router configuration
- `src/store`: Pinia store modules
- `src/views`: Page components
  - `Blog`: Blog-related views
  - `User`: User-related views
