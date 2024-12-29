const mysql = require('mysql2/promise');

// create connection
const pool = mysql.createPool({
  host: 'localhost',
  user: 'root', // modify if using another user
  password: 'password', // enter your password
  database: 'softeng24_tolls', // database name
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

module.exports = pool;