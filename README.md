# PPCP Onboarding Normalizer





## Setup Instructions

1. [Install gradle](https://docs.gradle.org/current/userguide/installation.html) and project dependencies:

  ```
  ./gradlew build -x test
  ```

2. Start server:

  ```
  java -jar build/libs/OnboardingJSONInterpreter-0.1.jar
  ```

  This starts the server on port `8080` listening to all interfaces.

## Deploying to Heroku

You can deploy this app directly to Heroku to see the app live. Skip the setup instructions above and click the button below. This will walk you through getting this app up and running on Heroku in minutes.

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/zcratx/PPCP-Onboarding)

## Pro Tips

* Run `java -Dserver.port=4000 -jar build/libs/OnboardingJSONInterpreter-0.1` to start the server on port 4000. Replace `4000` with any number to start it on a different port.

## Disclaimer

On guy coding. There might be bugs. Be careful and use it at your own RISK!
