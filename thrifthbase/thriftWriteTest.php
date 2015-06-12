<?php
$GLOBALS['THRIFT_SRC'] = '/home/kaniu/thrift/lib/php/lib/Thrift';
$GLOBALS['THRIFT_ROOT'] = '/usr/lib/php/Thrift';

require_once $GLOBALS['THRIFT_ROOT'].'/autoload.php' ;
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift.php' ;
require_once $GLOBALS['THRIFT_ROOT'].'/Transport/TTransport.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Protocol/TProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Protocol/TBinaryProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Transport/TBufferedTransport.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Type/TMessageType.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Factory/TStringFuncFactory.php';
require_once $GLOBALS['THRIFT_ROOT'].'/StringFunc/TStringFunc.php';
require_once $GLOBALS['THRIFT_ROOT'].'/StringFunc/Core.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Type/TType.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Exception/TException.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Exception/TTransportException.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Exception/TProtocolException.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Hbase.php' ;
require_once $GLOBALS['THRIFT_ROOT'].'/Types.php' ;

#use Thrift;
use Thrift\Transport\TBufferedTransport; 
use Thrift\Transport\TTransport;
use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\TSocket;
use Thrift\Transport\TSocketPool;
use Thrift\Transport\TFramedTransport;

use Hbase\Mutation;
use Hbase\HbaseClient;

$hbase_server='localhost';#'64.203.107.140'
$socket = new TSocket( $hbase_server , 9090 );
$socket->setSendTimeout( 20000 ); 
$socket->setRecvTimeout( 40000 ); 
$transport = new TBufferedTransport( $socket );
$protocol = new TBinaryProtocol( $transport );
$client = new HbaseClient( $protocol );

$transport->open();


###write to table 'writehist' table
$tablename="writehist";
$attributes=array( );
##there are no predefined value types. All values are stored as bytes. Use native type: string, int, array

$mutation =new Mutation(array(
	'column' => 'stpr:callid',
	 'value'=> 'callidtest'));

$mutation1 =new Mutation(array(
        'column' => 'stpr:status',
         'value'=> 'processing'));


$mutation2 =new Mutation(array(
        'column' => 'stpr:priority',
         'value'=> 'low'));

$mutation3 =new Mutation(array(
        'column' => 'vals:pkid',
         'value'=> 'testpk'));

$mutation4 =new Mutation(array(
        'column' => 'vals:pkfield',
         'value'=> 'vani'));

$mutation5 =new Mutation(array(
        'column' => 'vals:pkidfinal',
         'value'=> 'vani'));

$mutation6 =new Mutation(array(
        'column' => 'vals:field',
         'value'=> 'vani'));

$mutation7 =new Mutation(array(
        'column' => 'vals:oldval',
         'value'=> 'vani'));

$mutation8 =new Mutation(array(
        'column' => 'vals:newval',
         'value'=> 'vani'));

$mutation9 =new Mutation(array(
        'column' => 'vals:newvalfinal',
         'value'=> 'vani'));

$mutation10 =new Mutation(array(
        'column' => 'act:requesttime',
         'value'=> 'vani'));

$mutation11 =new Mutation(array(
        'column' => 'act:approvaltime',
         'value'=> 'vani'));

$mutation12 =new Mutation(array(
        'column' => 'act:writetime',
         'value'=> 'vani'));

$mutation13 =new Mutation(array(
        'column' => 'act:approver',
         'value'=> 'vani'));

$mutation14 =new Mutation(array(
        'column' => 'act:requester',
         'value'=> 'vani'));

$mutation15 =new Mutation(array(
        'column' => 'act:action',
         'value'=> 'vani'));

##note all columns are optional, therefore if a value is not specified you may ignore creating a Mutation for the column.

$mutations=array( $mutation ,  $mutation1 ,$mutation2 , $mutation3 , $mutation4 , $mutation5 , $mutation6 , $mutation7 , $mutation8 , $mutation9 , $mutation10 ,$mutation11 ,$mutation12 ,$mutation13 ,$mutation14 ,$mutation15 , );


##row_key is the write_id
$row_key=2;
$client->mutateRow( $tablename, $row_key, $mutations, $attributes );

//$mutation =new Mutation(array("column" => "info:name", "value" => "veena"));
//$client->mutateRow("table_name", $row_key=2, $mutation, $attributes );






//get column data
$row_name = $row_key;
$fam_col_name = 'stpr:status';
$arr = $client->get($tablename, $row_name, $fam_col_name, $attributes);

// $arr = array
foreach ($arr as $k => $v) {
	// $k = TCell
	echo " ------ get one : value = {$v->value} , <br>  ";
	echo " ------ get one : timestamp = {$v->timestamp}  <br>";
}

echo "----------\r\n";

$arr = $client->getRow($tablename, $row_name, $attributes);
// $client->getRow return a array
foreach ($arr as $k => $TRowResult) {
	// $k = 0 ; non-use
	// $TRowResult = TRowResult
	var_dump($TRowResult);
}


echo "----------\r\n";


$transport->close();
?>

