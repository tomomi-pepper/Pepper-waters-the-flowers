# Pepper Flower

[Tukeru](https://ssl.braveridge.com/store/html/products/detail.php?product_id=34)を使って、湿度を感知してPepperがお花に水をあげるプログラムです。

# Pepper
Pepper以下のソースコードはPepper上で動くプロジェクトファイル群です。

# Android

Android以下のソースコードは[Project Linking](https://linkingiot.com/)で配布されているSDKのサンプルアプリを一部加工したものです。

[Linking開発者サイト](https://linkingiot.com/developer/index.html)サイトから<code>sdaiflib.jar</code>を取得して
<code>Android/PepperFlowerApp/app/libs/</code>
に設置してください。

また、
<pre>Android/PepperFlowerApp/app/src/main/java/com/sample/nttdocomo/android/linkingpairingdemo/pairing/SensorDemoActivity.java</pre>
の321行目のURLのIPアドレスを各自の環境に合わせて変更してください。
<code>URL url = new URL("http://127.0.0.1/set_humidity.php?humidity=" + humidity);</code>

# Server
Server以下のソースコードは、PHPで動く湿度保存、取得用のサーバーのプログラムです。
MySQLを使用しています。
thermo_hygrometerという名前のテーブルが必要です。

<code>set_humidity.php</code>と<code>get_humidity.php</code>ともに以下のパラメータを環境に合わせて変えてください。
<pre>$server = 'localhost';
$username = 'root';
$password = 'xxxxxx';
$database = 'tomo';</pre>

