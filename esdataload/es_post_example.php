<?
function restgetcall() {
 $url="http://192.168.179.101:9200/firstnames_index/_analyze?analyzer=firstnames_synonyms&text='Frederick'";
 $headers = array(
 'Accept: application/json',
 'Content-Type: application/json',
 );
 $data = json_encode( $vars );
 
 $handle = curl_init();
 curl_setopt($handle, CURLOPT_URL, $url);
 curl_setopt($handle, CURLOPT_HTTPHEADER, $headers);
 curl_setopt($handle, CURLOPT_RETURNTRANSFER, true);
# curl_setopt($handle, CURLOPT_SSL_VERIFYHOST, false);
# curl_setopt($handle, CURLOPT_SSL_VERIFYPEER, false);
 
 curl_setopt($handle, CURLOPT_HTTPGET, true);
 
 $response = curl_exec($handle);
 $code = curl_getinfo($handle, CURLINFO_HTTP_CODE);

if ($response === false) {
    $info = curl_getinfo($handle);
    curl_close($handle);
    die('error occured during curl exec. Additional info: ' . var_export($info));
}
curl_close($handle);
$decoded = json_decode($response);
if (isset($decoded->response->status) && $decoded->response->status == 'ERROR') {
    die('error occured: ' . $decoded->response->errormessage);
}
echo 'response ok!';
print_r($decoded);
#var_export($decoded->response);

}

restgetcall();
?>
