<?php
ini_set( 'display_errors', 1 );

$server = 'localhost';
$username = 'root';
$password = 'xxxxxx';
$database = 'tomo';


function quote_smart($value)
{
    if (!is_numeric($value)) {
        $value = "'" . mysql_real_escape_string($value) . "'";
    }
    return $value;
}

function param_check_mode($value){
    return preg_match('/(0|1)/',$value);
}

function param_check_temperature($value){
    return preg_match('/^[+-]?(\d*\.\d+|\d+\.?\d*)/',$value);
}

function param_check_humidity($value){
    return preg_match('/^[+-]?(\d*\.\d+|\d+\.?\d*)/',$value);
}


function set_sql_parameters($html_parameter_list,&$sql_parameter_list){
    foreach( $html_parameter_list as $table => $html_p )
    {
        $sql_p = array();
        foreach( $html_p as $key => $array )
        {
            if($array[0] === 'required'){
                if(isset($_GET[$key]) and !is_array($_GET[$key]) and $_GET[$key] != '' and !empty($_GET[$key]) and $array[1]($_GET[$key]) === 1){
                    $sql_p += array($key=>quote_smart($_GET[$key]));
                }else{
                    unset($sql_p);
                    break;
                }
            }else{
                if( isset($_GET[$key]) and !is_array($_GET[$key]) and $_GET[$key] != '' and $array[1]($_GET[$key]) === 1){
                    $sql_p += array($key=>quote_smart($_GET[$key]));
                }
            }
        }
        if(isset($sql_p) and count($_GET) == count($sql_p) and count($_GET) != 0){
            $sql_parameter_list = $sql_p;
            return $table;
        }
    }
    exit();
}

function insert($con,$table,$fields)
{
    global $database;
    $sql = 'INSERT INTO ' . $database . '.' . $table;
    $sql .= '(';
    $sql .= implode( ',', array_keys($fields) );
    $sql .= ',updatetime';
    $sql .= ')';
    $sql .= 'VALUES(';
    $sql .= implode( ',', $fields );
    $sql .= ',now()';
    $sql .= ')';
    $result_flag = mysqli_query($con,$sql);
    if (!$result_flag) {
        $ret = array('results' =>'fail' . 'insert error.');
        echo json_encode($ret);
        die('insert error.');
    }
}


$thermo_hygrometer_html_params = array(
    'mode' => array('option','param_check_mode'),
    'temperature' => array('option','param_check_temperature'),
    'humidity' => array('required','param_check_humidity'),
    );

$html_params_list = array(
    'thermo_hygrometer' => $thermo_hygrometer_html_params,
    );


$sql_parameter = array();

$con = mysqli_connect($server, $username, $password);
if (!$con) {
    $ret = array('results' =>'fail' . 'connection error.');
    echo json_encode($ret);
    die('connection error.');
}

$table = set_sql_parameters($html_params_list,$sql_parameter);

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

insert($con,$table,$sql_parameter);

mysqli_close($con);
$ret = array('results' =>'success');
echo json_encode($ret);



?>
