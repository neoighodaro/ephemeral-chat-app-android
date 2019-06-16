const express = require('express');
const bodyParser = require('body-parser');
const Chatkit = require('@pusher/chatkit-server');
const app = express();

const chatkit = new Chatkit.default({
  instanceLocator: 'v1:us1:cc3aba0a-bc81-457d-924b-d1a1ba11dfdd',
  key:
    'afb217fd-319b-44ab-b0b4-51e8e1578e17:w04ZBZREsP9r1zyMZnfvqMlMtxsy23dzMkN2tUeRquc='
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.post('/token', (req, res) => {
  const result = chatkit.authenticate({
    userId: req.query.user_id
  });
  res.status(result.status).send(result.body);
});

app.post('/delete-message', (req, res) => {
  const { messageId, timer } = req.body;
  setTimeout(() => {
    chatkit
      .deleteMessage({
        id: messageId
      })
      .then(() => console.log('deleted'))
      .catch(err => console.error(err));
  }, timer);
  res.end();
});

const server = app.listen(3000, () => {
  console.log(`Express server running on port ${server.address().port}`);
});
