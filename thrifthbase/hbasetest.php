$service_url = 'http://10.77.201.101:8888/consumer_data/unique123';
 $curl = curl_init($service_url);
 $curl_post_data = array(
            "person:firstname" => 'kaniu',
            "person:lastname" => 'ndungu',
            );
 curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
 curl_setopt($curl, CURLOPT_POST, true);
 curl_setopt($curl, CURLOPT_POSTFIELDS, $curl_post_data);
 $curl_response = curl_exec($curl);
 curl_close($curl);
 
 $xml = new SimpleXMLElement($curl_response);
