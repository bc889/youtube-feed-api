# Youtube Feed API (YFA)
A REST API service for subscribing to YouTube channel feeds
that's built around Google's [PubSubHubbub hub](https://pubsubhubbub.appspot.com/).

`POST /users/{userId}/subscriptions` will subscribe to the given channels on the users behalf and:
1. Save updates to a feed the user can access (GET /users/{userId}/feed)
2. Send notifications to the user via [Pushover](https://pushover.net/), user must provide a userId and key from /users/{{test_user_id}}/notification-settings.

This is an active work in progress; instructions for running locally coming soon.