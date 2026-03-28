class Box
begin
  var size: integer;
end;

var b: Box;

begin
  b := Box.Create();
  b.size := 25;
  writeln(b.size);
end.
