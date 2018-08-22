/*
* sample-github-app webhook handler
* @flow-strict
*/

var createHandler = require('github-webhook-handler');
var createApp = require('github-app');
var http = require('http');
var express = require('express');

const server = express();
server.get('/', (req, res) => res.send('Hello World!'));
server.listen(7777, () => console.log('Example app listening on port 7777!'));

// http.createServer(function (req, res) {
//   console.log('Server created!');
//   handler(req, res, function (err) {
//     res.statusCode = 404
//     res.end('no such location')
//   });
// }).listen(7777);

var app = createApp({
  id: process.env.APP_ID,
  cert: require('fs').readFileSync('sample-github-app-jl.private-key.pem')
});

var handler = createHandler({
  path: '/',
  secret: 'mywebhooksecret',
});

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
