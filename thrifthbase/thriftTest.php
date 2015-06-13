<?php
$GLOBALS['THRIFT_ROOT'] = '/home/<homedir>/thrift/lib/php/lib';

require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/autoload.php' ;
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Thrift.php' ;
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Transport/TTransport.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Protocol/TProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Protocol/TBinaryProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Transport/TBufferedTransport.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Type/TMessageType.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Factory/TStringFuncFactory.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/StringFunc/TStringFunc.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/StringFunc/Core.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Type/TType.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Exception/TException.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Exception/TTransportException.php';
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Exception/TProtocolException.php';

require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Hbase.php' ;
require_once $GLOBALS['THRIFT_ROOT'].'/Thrift/Types.php' ;

#use Thrift;
use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\TSocket;
use Thrift\Transport\TSocketPool;
use Thrift\Transport\TFramedTransport;
use Thrift\Transport\TBufferedTransport;
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

