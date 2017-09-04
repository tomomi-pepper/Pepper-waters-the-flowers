<?php
ini_set( 'display_errors', 1 );

$server = 'localhost';
$username = 'root';
$password = 'xxxxxx';
$database = 'tomo';

function select($con,$table)
{
    global $database;
    $sql = 'select * from ' + $database + '.thermo_hygrometer ORDER BY updatetime DESC LIMIT 1';
    if ($result = mysqli_query($con,$sql)) {
        while ($row = $result->fetch_assoc()) {
            return  (double)$row["humidity"];
        }
        // 結果セットを閉じる
        $result->close();
    }
    return 0.0;
}

$con = mysqli_connect($server, $username, $password);
if (!$con) {
    $ret = array('results' =>'fail' . 'connection error.');
    echo json_encode($ret);
    die('connection error.');
}

$table = 'thermo_hygrometer';

if($table === ''){
    $ret = array('results' =>'fail' . 'parameter error.');
    echo json_encode($ret);
    die('parameter error');
}

$db_selected = mysqli_select_db($con, $database);
if (!$db_selected){
    $ret = array('results' =>'fail' . 'database error.');
    echo json_encode($ret);
    die('database error.');
}

$humidity = select($con,$table);

mysqli_close($con);
$ret = array('results' =>$humidity);
echo json_encode($ret);



?>
