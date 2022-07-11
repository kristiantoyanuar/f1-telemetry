# F1 Telemetry

This project show a simulation of telemtry dashboard with two roles : anonymous and team user to visualize telemetry of speed, gear and brake condition of a F1 car. 

## Running the application

How to build this project:

```No you dont need, it took ages, jump to next```

This project requires Docker installed. Then run: 

```docker build -t kris/f1-telemetry .```

once done, fire up:

```docker-compose up```

if everything goes right then open [http://localhost:8080/](http://localhost:8080/)

you can login on http://localhost:8080/login with user `user` and password `user`