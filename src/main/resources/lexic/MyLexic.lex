Real : 1 : (+|-|\?)((\d(\d)*) | (\d(\d)*). | .(\d(\d)*) | (\d(\d)*).(\d(\d)*))(\? | ((e|E)(+|-|\?) (\d(\d)*)));
Int : 2 : (+|-|\?)(\d(\d*));
KeyWord : 2 : lambda | cond | if | define;
OpenBkt : 0 : \(;
CloseBkt : 0 : \);
Space : 0 : \s(\s)*;
Identify : 0 : (\w | + | - | \* | / | % | ! | _ | \d)(\w | + | - | \* | / | % | ! | _ | \d)* | \|\.(\.*)\|;