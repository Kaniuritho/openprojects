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

//Utility method
function printRow( $rowresult ) {
  $attrbt=array();
  echo( "row: {$rowresult->row}, cols: \n" );
  $values = $rowresult->columns;
  asort( $values );
  foreach ( $values as $k=>$v ) {
    echo( "  {$k} => {$v->value}\n" );
  }
}
 function printRow2($arr){
	var_dump('count of result :'.count($arr));
	foreach ($arr as $k => $v) {
        // code...
        var_dump($v);
	#echo( "  {$k} => {$v->$value}\n" );
	}
}

$hbase_server_ip = 'localhost';
$socket = new TSocket( $hbase_server_ip, 9090 );
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


echo "----scanner get ------\r\n";

# Run a scanner on the rows we just created
echo( "----Scan a column family  LIMITING RESULTS COUNT...\n" );
$startrow = '';
$columns = array ('column' => 'stpr', );
$scanner = $client->scannerOpen($tablename, $startrow, $columns, $attributes);
$nbRows = 1000;

try {
 printRow2( $client->scannerGetList( $scanner, $nbRows ) );
} catch ( NotFound $nf ) {
  $client->scannerClose( $scanner );
  echo( "Scanner finished\n" );
}


$client->scannerClose($scanner);

echo( "----Scan a column in a column family WITHOUT LIMITING RESULTS COUNT...\n" );
$startrow = '1';
$columns = array ('column' => 'stpr:status', );
$scanner = $client->scannerOpen($tablename, $startrow, $columns, $attributes);
try {
   printRow2( $client->scannerGet( $scanner ) );
} catch ( NotFound $nf ) {
  $client->scannerClose( $scanner );
  echo( "Scanner finished\n" );
}


echo( "----Scan several columns in different column families WITHOUT LIMITING RESULTS COUNT..\n" );
$startrow = '1';
$columns = array ('stpr:status','vals:field' );
$scanner = $client->scannerOpen($tablename, $startrow, $columns, $attributes);
try {
   printRow2( $client->scannerGet( $scanner ) );
} catch ( NotFound $nf ) {
  $client->scannerClose( $scanner );
  echo( "Scanner finished\n" );
}


echo( "----NOTE: THRIFT HAS NOT IMPLEMENTED FILTERS YET....COMING SOON!  Filter by column value ..\n" );



echo( "----Using restful scan filter ---  Filter by column value ..\n" );
$filter_str1 ='<Scanner startRow="1" batch="10">
<filter>
	{"latestVersion":true,
	"ifMissing":true,
	"qualifier":"status",
	"family":"stpr",
	"op":"EQUAL",
	"type":"SingleColumnValueFilter",
	"comparator":{"value":"vani","type":"BinaryComparator"}}
</filter></Scanner>';

$filter_str2 ="<Scanner startRow='1' filter=
'{
  'type': 'RowFilter',
  'op': 'EQUAL',
  'comparator': {
    'type': 'BinaryComparator',
    'value': '1'
  }
}'></Scanner>";

$filter_str ="<Scanner startRow='1'></Scanner>";

if(is_callable('curl_init')){
   echo "Enabled\n";
}
else
{
   echo "Not enabled\n";
}

$ch = curl_init();  
$headers2 = array(
    "Content-type: text/xml",
    "Content-length: ".strlen($filter_str),
    "Connection: close",
);
$headers= "Content-type: text/xml";

$hbase_rest_url= "http://{$hbase_server_ip}:8080/{$tablename}/scanner";
#$hbase_rest_url= "http://{$hbase_server_ip}:8080/{$tablename}/1/stpr:status";
echo "$hbase_rest_url \n";
 
curl_setopt($ch,CURLOPT_URL, $hbase_rest_url);

$data_string='{
  "Scanner": {
    "startRow": "1",
    "batch": "1"
}}';

$data_string2='{
  "Scanner": {
    "-startRow": "",
    "-batch": "10",
    "filter": 
        {"latestVersion":true,
        "ifMissing":true,
        "qualifier":"status",
        "family":"strp",
        "op":"EQUAL",
        "type":"SingleColumnValueFilter",
        "comparator":{"value":"vani","type":"BinaryComparator"}}
  }
}';

curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    'Content-Type: application/json',
    'Content-Length: ' . strlen($data_string))
);
curl_setopt($ch, CURLOPT_TIMEOUT, 5);
curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 5);

$output=curl_exec($ch);
 
curl_close($ch);
echo $output;

echo "----------\r\n";

$transport->close();
?>

