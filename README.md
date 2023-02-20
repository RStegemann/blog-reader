## How to run:
1. Open sbt console in ./blog-reader-backend
2. Use 'run' to start backend (Requires Java Runtime Environment for SDK 17+ as well as open TCP port 8080)
3. Open console in ./blog-reader-frontend
4. Run 'npm install' to setup packages
5. Run 'npm run start' to launch frontend
6. Frontend runs on "localhost:3000" and should be up for testing

## Disclaimers:
- Due to time constraints the code does not fully represent production ready code and the tests are very rough
- Some recommended improvements would include requiring authorization for websocket connections (for example based on IP), especially for the broadcast channel, increased exception handling for websockets and test setups for websockets.
- Furthermore another update should include the option to quit the application based on user input as well as allow configuration of websocket addresses. In testing this caused timeouts for Blogreader scheduler though.
