const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const morgan = require('morgan');
// const passport = require('passport');

// Router 연결부
const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');

const authRouter = require('./routes/auth');
const partnersRouter = require('./routes/partners');

// Sequelize, passport 연결부
const { sequelize } = require('./models');
// const passportConfig = require('./passport');

const app = express();
sequelize.sync(); // Sequelize Sync
// passportConfig(passport); // Passport

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(morgan('dev'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/auth', authRouter);
app.use('/partners', partnersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
