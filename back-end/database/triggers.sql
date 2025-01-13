USE paywaydb;
DELIMITER //
CREATE TRIGGER update_debt_on_pass
    AFTER INSERT ON Pass
    FOR EACH ROW
BEGIN
    DECLARE home_op_id VARCHAR(5);
    DECLARE station_op_id VARCHAR(5);

    -- Get the home operator ID of the tag
    SELECT op_id INTO home_op_id
    FROM Tag
    WHERE tag_ref = NEW.tag_ref;

    -- Get the operator ID of the toll station
    SELECT op_id INTO station_op_id
    FROM TollStation
    WHERE station_id = NEW.station_id;

    -- Check if they are different operators
    IF home_op_id != station_op_id THEN
        -- Update the Debt table
        INSERT INTO Debt (from_op_id, to_op_id, debt_amount, last_update)
        VALUES (home_op_id, station_op_id, NEW.charge, CURRENT_TIMESTAMP)
        ON DUPLICATE KEY UPDATE
                                    debt_amount = debt_amount + NEW.charge,
                                    last_update = CURRENT_TIMESTAMP;
END IF;
END//
DELIMITER ;


DELIMITER //

CREATE TRIGGER update_debt_on_pass_delete
    AFTER DELETE ON Pass
    FOR EACH ROW
BEGIN
    DECLARE home_op_id VARCHAR(5);
    DECLARE station_op_id VARCHAR(5);

    -- Get the home operator ID of the tag
    SELECT op_id INTO home_op_id
    FROM Tag
    WHERE tag_ref = OLD.tag_ref;

    -- Get the operator ID of the toll station
    SELECT op_id INTO station_op_id
    FROM TollStation
    WHERE station_id = OLD.station_id;

    -- Check if they are different operators
    IF home_op_id != station_op_id THEN
        -- Update the Debt table
    UPDATE Debt
    SET debt_amount = debt_amount - OLD.charge,
        last_update = CURRENT_TIMESTAMP
    WHERE from_op_id = home_op_id AND to_op_id = station_op_id;

    -- Remove the debt row if the debt amount reaches zero
    DELETE FROM Debt
    WHERE from_op_id = home_op_id AND to_op_id = station_op_id AND debt_amount <= 0;
END IF;
END//

DELIMITER ;

DELIMITER //

CREATE TRIGGER update_debt_on_pass_update
    AFTER UPDATE ON Pass
    FOR EACH ROW
BEGIN
    DECLARE old_home_op_id VARCHAR(5);
    DECLARE old_station_op_id VARCHAR(5);
    DECLARE new_home_op_id VARCHAR(5);
    DECLARE new_station_op_id VARCHAR(5);

    -- Get the home operator ID of the tag (OLD values)
    SELECT op_id INTO old_home_op_id
    FROM Tag
    WHERE tag_ref = OLD.tag_ref;

    -- Get the operator ID of the toll station (OLD values)
    SELECT op_id INTO old_station_op_id
    FROM TollStation
    WHERE station_id = OLD.station_id;

    -- Get the home operator ID of the tag (NEW values)
    SELECT op_id INTO new_home_op_id
    FROM Tag
    WHERE tag_ref = NEW.tag_ref;

    -- Get the operator ID of the toll station (NEW values)
    SELECT op_id INTO new_station_op_id
    FROM TollStation
    WHERE station_id = NEW.station_id;

    -- Adjust the Debt table for OLD values if the operators were different
    IF old_home_op_id != old_station_op_id THEN
    UPDATE Debt
    SET debt_amount = debt_amount - OLD.charge,
        last_update = CURRENT_TIMESTAMP
    WHERE from_op_id = old_home_op_id AND to_op_id = old_station_op_id;

    -- Remove the debt row if the debt amount reaches zero
    DELETE FROM Debt
    WHERE from_op_id = old_home_op_id AND to_op_id = old_station_op_id AND debt_amount <= 0;
END IF;

-- Adjust the Debt table for NEW values if the operators are different
IF new_home_op_id != new_station_op_id THEN
        INSERT INTO Debt (from_op_id, to_op_id, debt_amount, last_update)
        VALUES (new_home_op_id, new_station_op_id, NEW.charge, CURRENT_TIMESTAMP)
        ON DUPLICATE KEY UPDATE
                                debt_amount = debt_amount + NEW.charge,
                                last_update = CURRENT_TIMESTAMP;
END IF;
END//

DELIMITER ;

DELIMITER //
CREATE TRIGGER delete_passes_before_tollstation
    BEFORE DELETE ON tollstation
    FOR EACH ROW
BEGIN
    -- Delete all passes associated with the station being deleted
    DELETE FROM pass
    WHERE station_id = OLD.station_id;
END//

DELIMITER ;

DELIMITER //
CREATE TRIGGER delete_tags_before_passes
    BEFORE DELETE ON tollstation
    FOR EACH ROW
BEGIN
    -- Delete all passes associated with the station being deleted
    DELETE FROM pass
    WHERE station_id = OLD.station_id;
END//

DELIMITER ;