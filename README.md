# Logistics

Have you ever heard the saying "if it works, don't break it?" Maybe I just invented that to excuse some awful code? Haha. But it works.

> It's not really awful code per-say. I intend to clean it up and re-organize it in my free time.

This is an open-sourced Spring Boot + Java microservice that could be used to power a logistics application.


## Features

- Authenticate Users, Riders and Companies via JWT.
- OTP Verification via Email.
- Approve Company accounts before onboarding. You can extend the API to an admin interface.
- Companies can onboard riders, and supply them login credentials.
- Paystack Integration on creating a delivery. Option for Cash.
- Riders being able to accept, pickup, cancel and order. Fundamentally some tweaked CRUD operations.
- API to supply reports, analytics to the companies on deliveries being done.
- Sending receipts and confirmation emails.

So much stuff, really. I cannot cover it all.

## The Stack

- Java 19
- Maven for managing dependencies.
- Spring Boot
- Postgres
- Docker

## Setup

- Clone this current repository.
- Next `cd boardend`
- Run `docker-compose up`
- Access the application on port 5000.
- Have fun.

## Contributing

Feel free, MIT License anyway.