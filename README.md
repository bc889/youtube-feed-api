# Youtube Feed API (YFA)
A REST API built around subscribing to RSS feeds for Youtube channels using google's [PubSubHubbub hub](https://pubsubhubbub.appspot.com/).

`POST /users/{userId}/subscriptions` will subscribe to the given channels on the users behalf and:
 1. Save updates to a pageable feed users can access at any time with `GET /users/{userId}/feed`.
 2. Send notificataions to the user via [Pushover](https://pushover.net/), must provide a userId and key from `PATCH /users/{{test_user_id}}/notification-settings`.

This is an active work in progress; instructions for running locally coming soon.
