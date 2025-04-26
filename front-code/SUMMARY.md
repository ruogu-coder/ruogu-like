# Billion-Level Like System Frontend Implementation

## Project Structure

The frontend for the Billion-Level Like System has been implemented using Vue 3, Vite, and Ant Design Vue. The application provides a user-friendly interface for users to register, login, view blogs, and like/unlike them.

```
front-code/
├── public/              # Static assets
├── src/
│   ├── api/             # API integration
│   │   ├── request.js   # Base Axios setup
│   │   ├── blog.js      # Blog API endpoints
│   │   ├── thumb.js     # Like/Unlike API endpoints
│   │   └── user.js      # User API endpoints
│   ├── assets/          # Images and other assets
│   ├── components/      # Reusable components
│   │   └── ThumbButton.vue # Like button component
│   ├── layouts/         # Layout components
│   │   └── DefaultLayout.vue # Main layout
│   ├── router/          # Vue Router configuration
│   │   └── index.js     # Routes definition
│   ├── store/           # Pinia store
│   │   ├── index.js     # Store setup
│   │   └── modules/     # Store modules
│   │       ├── blog.js  # Blog state management
│   │       └── user.js  # User state management
│   ├── views/           # Page components
│   │   ├── Blog/        # Blog-related views
│   │   │   ├── Detail.vue # Blog detail page
│   │   │   └── List.vue # Blog list page
│   │   ├── User/        # User-related views
│   │   │   ├── List.vue   # User management (admin)
│   │   │   ├── Login.vue  # Login page
│   │   │   ├── Password.vue # Change password page
│   │   │   ├── Profile.vue # User profile page
│   │   │   └── Register.vue # Registration page
│   │   ├── Home.vue     # Homepage
│   │   └── NotFound.vue # 404 page
│   ├── App.vue          # Root component
│   └── main.js          # Application entry point
├── .eslintrc.js         # ESLint configuration
├── .gitignore           # Git ignore file
├── index.html           # HTML template
├── jsconfig.json        # JavaScript configuration
├── package.json         # Project dependencies
├── README.md            # Project documentation
├── scripts/             # Utility scripts
│   ├── deploy.ps1       # Windows deployment script
│   └── deploy.sh        # Unix deployment script
└── vite.config.js       # Vite configuration
```

## Key Features

1. **User Management**
   - User registration with validation
   - User login with JWT authentication
   - Profile management
   - Password changing

2. **Blog Features**
   - Blog listing with pagination
   - Blog detail view
   - Markdown-like content rendering

3. **Like System**
   - Like/Unlike functionality
   - Real-time like count updates
   - Like status persistence

4. **Admin Features**
   - User listing and management
   - User status control (freeze/unfreeze)
   - User deletion

## Technologies Used

- **Vue 3**: Modern JavaScript framework with Composition API
- **Vite**: Fast build tool and development server
- **Ant Design Vue**: UI component library
- **Pinia**: State management
- **Vue Router**: Client-side routing
- **Axios**: HTTP client for API calls

## Next Steps

1. **Testing**: Add unit and integration tests
2. **Performance Optimization**: Implement lazy loading and code splitting
3. **Advanced Features**: Add comments, sharing, and more interactive elements
4. **Deployment**: Set up CI/CD for automated deployment 