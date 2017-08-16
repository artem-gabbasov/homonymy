<?php

  // Описание пакета phpmorphy взято по ссылке: http://valera.ws/2007.09.05~morpho_search_in_mysql/
  // The description of the phpmorphy package was taken from http://valera.ws/2007.09.05~morpho_search_in_mysql/

	require_once('phpmorphy/src/common.php');

	$opts = array(
		'storage' => PHPMORPHY_STORAGE_MEM,
		'with_gramtab' => false,
		'predict_by_suffix' => true, 
		'predict_by_db' => true
	);
	
	$dir = 'phpmorphy/dicts';
	
	// Create descriptor for dictionary located in $dir directory with russian language
	$dict_bundle = new phpMorphy_FilesBundle($dir, 'rus');
	
	// Create phpMorphy instance
	$morphy = new phpMorphy($dict_bundle, $opts);
	
	setlocale(LC_CTYPE, 'ru_RU.cp1251');
	
	if ($argc > 1)
	{
	  //$wordForm = my_strtoupper('светит');//$argv[1]);
	  $wordForm = my_strtoupper($argv[1]);
	  echo "<root>\n" . variants_to_xml($morphy, $wordForm, "\t") . "</root>\n";
	}

function variants_to_xml($morphy, $wordForm, $outerTabs)
{
  $innerTabs = $outerTabs . "\t";
  
  $lemmas = $morphy->lemmatize($wordForm);
  $gramInfo = $morphy->getGramInfo($wordForm);
  $out = $outerTabs . "<wordForm>" . my_strtolower($wordForm) . "\n";
  $innerTabsLevel2 = $innerTabs . "\t";
  $i = 0;
  foreach($lemmas as $l)
  {
    $out .= $innerTabs . "<variant>\n";
    $out .= $innerTabsLevel2 . "<lemma>" . my_strtolower($l) . "</lemma>\n";
    $out .= gram_to_xml($gramInfo[$i], $innerTabsLevel2);
    $out .= $innerTabs . "</variant>\n";  
    $i++;
  }
  $out .= $outerTabs . "</wordForm>\n";
  return $out;
}
  
function gram_to_xml($info, $outerTabs)
{
  $innerTabs = $outerTabs . "\t";
  $out = "";
  foreach($info as $gram)
  {
    $out .= $outerTabs . "<gram>\n";
    $out .= $innerTabs . "<partOfSpeech>" . $gram['pos'] . "</partOfSpeech>\n";
    $out .= $innerTabs . "<grammemes>" . print_array($gram['grammems']) . "</grammemes>\n";
    $out .= $innerTabs . "<form_no>" . $gram['form_no'] . "</form_no>\n";
    $out .= $outerTabs . "</gram>\n";
  }
  return $out;
}  

function print_array($array)
{
  $out = "";
  
  if ($c = count($array))
  {
    $out .= $array[0];
    for($i = 1; $i < $c; $i++)
    {
      $out .= ", " . $array[$i];
    }
  }
  
  return $out;
}
  
function echo_as_tree($info)
{
  if (is_array($info))
  {
    echo "<";
    $first = true;
    foreach($info as $el)
    {
      if (!$first)
        echo ", ";
      echo_as_tree($el);
      $first = false;
    }
    echo ">";
  }
  else
    echo $info;
}
    
function my_strtoupper($string){ 
  return strtr($string, array( 
      "а" => "А", 
      "б" => "Б",
      "в" => "В",
      "г" => "Г",
      "д" => "Д",
      "е" => "Е", 
      "ё" => "Ё",
      "ж" => "Ж",
      "з" => "З", 
      "и" => "И",
      "й" => "Й",
      "к" => "К",
      "л" => "Л",
      "м" => "М",
      "н" => "Н", 
      "о" => "О",
      "п" => "П",
      "р" => "Р",
      "с" => "С",
      "т" => "Т", 
      "у" => "У",
      "ф" => "Ф",
      "х" => "Х",
      "ц" => "Ц",
      "ч" => "Ч",
      "ш" => "Ш",
      "щ" => "Щ",
      "ъ" => "Ъ", 
      "ы" => "Ы",
      "ь" => "Ь", 
      "э" => "Э", 
      "ю" => "Ю", 
      "я" => "Я",
    )); 
} 

function my_strtolower($string){ 
  return strtr($string, array( 
      "А" => "а", 
      "Б" => "б",
      "В" => "в",
      "Г" => "г",
      "Д" => "д",
      "Е" => "е", 
      "Ё" => "ё",
      "Ж" => "ж",
      "З" => "з", 
      "И" => "и",
      "Й" => "й",
      "К" => "к",
      "Л" => "л",
      "М" => "м",
      "Н" => "н", 
      "О" => "о",
      "П" => "п",
      "Р" => "р",
      "С" => "с",
      "Т" => "т", 
      "У" => "у",
      "Ф" => "ф",
      "Х" => "х",
      "Ц" => "ц",
      "Ч" => "ч",
      "Ш" => "ш",
      "Щ" => "щ",
      "Ъ" => "ъ", 
      "Ы" => "ы",
      "Ь" => "ь", 
      "Э" => "э", 
      "Ю" => "ю", 
      "Я" => "я",
    )); 
}   
?>
