<?php
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

#use Thrift;
use Thrift\Transport\TBufferedTransport; 
use Thrift\Transport\TTransport;
use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\TSocket;
use Thrift\Transport\TSocketPool;
use Thrift\Transport\TFramedTransport;

use Hbase\HbaseClient;


$socket = new TSocket( 'localhost', 9090 );
$socket->setSendTimeout( 20000 ); 
$socket->setRecvTimeout( 40000 ); 
$transport = new TBufferedTransport( $socket );
$protocol = new TBinaryProtocol( $transport );
#$protocol::$TBINARYPROTOCOLACCELERATED = '\Thrift\Protocol\TBinaryProtocolAccelerated';
$client = new HbaseClient( $protocol );

$transport->open();


//show all tables
$tables = $client->getTableNames();
foreach ( $tables as $name ) {
echo( "  found: {$name}\n" );
}
$transport->close();
?>

