const mysql = require('mysql2/promise');

// Use db = require('./dal/db') in each file for access to the database

// create connection
const pool = mysql.createPool({
  host: 'localhost',
  user: 'root', // modify if using another user
  password: 'password', // enter your password
  database: 'db_softeng24-25', // database name
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

module.exports = pool;