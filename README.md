# markov-chain-groupme-bot

All that you need to do is supply the resources/trainingtext.text file with the text you want, and give the send-request function in generator.clj the id of you groupme bot.

You can manually call make text in the REPL, or if you deploy to a hosting service and provide that url to groupme as the callback for the bot, the bot will send messages.

The "commands" to get the bot to send messages are:

"bot"- a randomly seeded test using markov chain
"bot *seedword*", where *seedword* is the word you want to start the markov chain.

The Procfile contains the starting instructions for hosting services like Heroku

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2020 FIXME
