/*
* sample-github-app webhook handler
* @flow-strict
*/

var createHandler = require('github-webhook-handler');
var createApp = require('github-app');
var http = require('http');
var express = require('express');
const SmeeClient = require('smee-client');
const WebhooksApi = require('@octokit/webhooks');
const APP_ID = process.env.GITHUB_APP_IDENTIFIER;
const WEBHOOK_SECRET = process.env.GITHUB_WEBHOOK_SECRET;

// const server = express();
// server.get('/', (req, res) => res.send('Hello World!'));
// server.post('/', (req, res) => res.send('posted!'));
// server.listen(7777, () => console.log('Example app listening on port 7777!'));

//create basic smee webserver
const smee = new SmeeClient({
  source: 'https://smee.io/x8YU2OFaKL6twd1',
  target: 'http://localhost:7777/events',
  logger: console
});
const events = smee.start();
events.close();

var handler = createHandler({
  path: '/',
  secret: WEBHOOK_SECRET,
});

// //create basic server
// http.createServer(function (req, res) {
//   handler(req, res, function (err) {
//     res.statusCode = 404
//     res.end('no such location')
//   });
//   console.log('Server created!');
// }).listen(7777);

var app = createApp({
  id: APP_ID,
  cert: require('fs').readFileSync('sample-github-app-jl.2018-08-29.private-key.pem')
});
console.log('App created!');

const webhooks = new WebhooksApi({
  secret: WEBHOOK_SECRET
});

webhooks.on('*', ({id, name, payload}) => {
  console.log(name, 'event received')
});

http.createServer(webhooks.middleware).listen(7777);

handler.on('push', function (event) {
  console.log(
    'Received a push event for %s to %s',
    event.payload.repository.name,
    event.payload.ref,
  );
  var installation = event.payload.installation.id;

  app.asInstallation(installation).then(function (github) {
    github.issues.createComment({
      owner: event.payload.repository.owner.login,
      repo: event.payload.repository.name,
      number: event.payload.issue.number,
      body: 'This is a test.'
    });
  });
});
