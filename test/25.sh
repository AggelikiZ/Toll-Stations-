#!/bin/bash
# Συνάρτηση για παύση μεταξύ των εντολών
pause() {
    read -p "Press any key to resume..." -n1 -s
    echo ""
}

./se2425 logout
pause

./se2425 login --username admin --passw 1234
pause

./se2425 healthcheck
pause

./se2425 resetpasses
pause

./se2425 healthcheck
pause

./se2425 resetstations
pause

./se2425 healthcheck
pause

./se2425 admin --addpasses --source passes25.csv
pause

./se2425 healthcheck
pause

./se2425 tollstationpasses --station AM08 --from 20220127 --to 20220210 --format json
pause

./se2425 tollstationpasses --station NAO04 --from 20220127 --to 20220210 --format csv
pause

./se2425 tollstationpasses --station NO01 --from 20220127 --to 20220210 --format csv
pause

./se2425 tollstationpasses --station OO03 --from 20220127 --to 20220210 --format csv
pause

./se2425 tollstationpasses --station XXX --from 20220127 --to 20220210 --format csv
pause

./se2425 tollstationpasses --station OO03 --from 20220127 --to 20220210 --format YYY
pause

./se2425 errorparam --station OO03 --from 20220127 --to 20220210 --format csv
pause

./se2425 tollstationpasses --station AM08 --from 20220128 --to 20220208 --format json
pause

./se2425 tollstationpasses --station NAO04 --from 20220128 --to 20220208 --format csv
pause

./se2425 tollstationpasses --station NO01 --from 20220128 --to 20220208 --format csv
pause

./se2425 tollstationpasses --station OO03 --from 20220128 --to 20220208 --format csv
pause

./se2425 tollstationpasses --station XXX --from 20220128 --to 20220208 --format csv
pause

./se2425 tollstationpasses --station OO03 --from 20220128 --to 20220208 --format YYY
pause

./se2425 passanalysis --stationop AM --tagop NAO --from 20220127 --to 20220210 --format json
pause

./se2425 passanalysis --stationop NAO --tagop AM --from 20220127 --to 20220210 --format csv
pause

./se2425 passanalysis --stationop NO --tagop OO --from 20220127 --to 20220210 --format csv
pause

./se2425 passanalysis --stationop OO --tagop KO --from 20220127 --to 20220210 --format csv
pause

./se2425 passanalysis --stationop XXX --tagop KO --from 20220127 --to 20220210 --format csv
pause

./se2425 passanalysis --stationop AM --tagop NAO --from 20220128 --to 20220208 --format json
pause

./se2425 passanalysis --stationop NAO --tagop AM --from 20220128 --to 20220208 --format csv
pause

./se2425 passanalysis --stationop NO --tagop OO --from 20220128 --to 20220208 --format csv
pause

./se2425 passanalysis --stationop OO --tagop KO --from 20220128 --to 20220208 --format csv
pause

./se2425 passanalysis --stationop XXX --tagop KO --from 20220128 --to 20220208 --format csv
pause

./se2425 passescost --stationop AM --tagop NAO --from 20220127 --to 20220210 --format json
pause

./se2425 passescost --stationop NAO --tagop AM --from 20220127 --to 20220210 --format csv
pause

./se2425 passescost --stationop NO --tagop OO --from 20220127 --to 20220210 --format csv
pause

./se2425 passescost --stationop OO --tagop KO --from 20220127 --to 20220210 --format csv
pause

./se2425 passescost --stationop XXX --tagop KO --from 20220127 --to 20220210 --format csv
pause

./se2425 passescost --stationop AM --tagop NAO --from 20220128 --to 20220208 --format json
pause

./se2425 passescost --stationop NAO --tagop AM --from 20220128 --to 20220208 --format csv
pause

./se2425 passescost --stationop NO --tagop OO --from 20220128 --to 20220208 --format csv
pause

./se2425 passescost --stationop OO --tagop KO --from 20220128 --to 20220208 --format csv
pause

./se2425 passescost --stationop XXX --tagop KO --from 20220128 --to 20220208 --format csv
pause

./se2425 chargesby --opid NAO --from 20220127 --to 20220210 --format json
pause

./se2425 chargesby --opid GE --from 20220127 --to 20220210 --format csv
pause

./se2425 chargesby --opid OO --from 20220127 --to 20220210 --format csv
pause

./se2425 chargesby --opid KO --from 20220127 --to 20220210 --format csv
pause

./se2425 chargesby --opid NO --from 20220127 --to 20220210 --format csv
pause

./se2425 chargesby --opid NAO --from 20220128 --to 20220208 --format json
pause

./se2425 chargesby --opid GE --from 20220128 --to 20220208 --format csv
pause

./se2425 chargesby --opid OO --from 20220128 --to 20220208 --format csv
pause

./se2425 chargesby --opid KO --from 20220128 --to 20220208 --format csv
pause

./se2425 chargesby --opid NO --from 20220128 --to 20220208 --format csv
pause
