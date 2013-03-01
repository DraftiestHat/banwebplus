<?php
require_once(dirname(__FILE__)."/globals.php");

function get_post_var($postname) {
	return isset($_POST[$postname])?$_POST[$postname]:'';
}

function my_session_start() {
	global $session_started;
	if ($session_started === FALSE) {
			$session_started = TRUE;
			session_start();
	}
}

function login_session($o_user) {
	my_session_start();
	$_SESSION['username'] = $o_user->get_name();
	$_SESSION['last_activity'] = time();
	$_SESSION['crypt_password'] = urlencode($o_user->get_crypt_password());
	$_SESSION['loggedin'] = 1;
}

function logout_session() {
	my_session_start();
	unset($_SESSION['username']);
	unset($_SESSION['last_activity']);
	unset($_SESSION['crypt_password']);
	unset($_SESSION['loggedin']);
}

function draw_page_head() {
	global $global_path_to_jquery;
	$a_page = array();
	$a_page[] = "<html>";
	$a_page[] = "<head>";
	$a_page[] = "<link href='/css/main.css' rel='stylesheet' type='text/css'>";
	$a_page[] = "<link href='/css/login_logout.css' rel='stylesheet' type='text/css'>";
	$a_page[] = '<script src="'.$global_path_to_jquery.'"></script>';
	$a_page[] = '<script src="/js/ajax.js"></script>';
	$a_page[] = '<script src="/js/login_logout.js"></script>';
	$a_page[] = "</head>";
	$a_page[] = "<body>";
	$a_page[] = "<table class='main_page_container'><tr><td class='centered'>";
	$a_page[] = "<table class='main_page_content'><tr><td>";
	$a_page[] = "<table style='border:2px solid black;border-radius:5px;padding:15px 30px;margin:0 auto;background-color:#fff;'><tr><td>";
	return implode("\n", $a_page);
}

function draw_page_foot() {
	$a_page = array();
	$a_page[] = "</td></tr></table>";
	$a_page[] = "</td></tr></table>";
	$a_page[] = "</td></tr></table>";
	$a_page[] = "</body>";
	$a_page[] = "</html>";
	return implode("\n", $a_page);
}
?>