# Frontend Module

This is the frontend module for the Modulith Learning project. It's a React-based Single Page Application (SPA) that uses Material-UI (MUI) for the component library.

## Technology Stack

- **React 18**: A JavaScript library for building user interfaces
- **TypeScript**: For type safety and better developer experience
- **Material-UI (MUI)**: React component library implementing Google's Material Design
- **React Router**: For navigation and routing
- **Axios**: For API communication with the backend

## Project Structure

```
frontend/
├── public/                 # Static files
│   ├── index.html          # HTML template
│   └── manifest.json       # PWA manifest
├── src/                    # Source code
│   ├── components/         # Reusable UI components
│   │   └── Header.tsx      # Application header
│   ├── pages/              # Page components
│   │   ├── Dashboard.tsx   # Dashboard page
│   │   └── NotFound.tsx    # 404 page
│   ├── services/           # Services for API communication
│   │   └── api.ts          # Axios configuration
│   ├── App.tsx             # Main application component
│   ├── index.tsx           # Application entry point
│   ├── index.css           # Global styles
│   ├── theme.ts            # MUI theme configuration
│   └── reportWebVitals.ts  # Performance monitoring
├── package.json            # NPM dependencies and scripts
├── tsconfig.json           # TypeScript configuration
└── build.gradle.kts        # Gradle build configuration
```

## Development

### Prerequisites

- Node.js (v20.12.2 or later)
- npm (v10.5.0 or later)

### Available Scripts

- **Install Dependencies**: `npm install`
- **Start Development Server**: `npm start`
- **Build for Production**: `npm run build`
- **Run Tests**: `npm test`

### Gradle Integration

The frontend module is integrated with Gradle using the node-gradle plugin. You can use the following Gradle tasks:

- **Install Dependencies**: `./gradlew :frontend:npmInstall`
- **Start Development Server**: `./gradlew :frontend:npmStart`
- **Build for Production**: `./gradlew :frontend:npmBuild`
- **Run Tests**: `./gradlew :frontend:npmTest`
- **Clean**: `./gradlew :frontend:clean`

## Communication with Backend

The frontend communicates with the backend using Axios. The API service is configured in `src/services/api.ts` and provides:

- Base URL configuration
- Authentication via JWT tokens
- Error handling for common HTTP status codes

## Deployment

To build the frontend for production, run:

```
./gradlew :frontend:npmBuild
```

This will create a production-ready build in the `frontend/build` directory, which can be served by any static file server.
