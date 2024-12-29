const db = require('./db');

// Write here all the scripts for the endpoints to implement.
// For example here is the getTollStationPasses endpoint
// and its derived query mysql

// SOS //
// In each file that needs to interact with the database add this line of code:
// const { getTollStationPasses } = require('../../dal/dal');

const getTollStationPasses = async (tollStationID, dateFrom, dateTo) => {
  const query = `
    SELECT passID, timestamp_ID, tollID, tagHomeID, tagRef, charge
    FROM Passes
    WHERE tollID = ? AND timestamp_ID >= ? AND timestamp_ID <= ?`;

  const [rows] = await db.execute(query, [tollStationID, dateFrom, dateTo]);
  return rows;
};

module.exports = {
  getTollStationPasses,
};