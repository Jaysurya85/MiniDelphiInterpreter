var
  x: integer;

function getX();
begin
  return x;
end;

begin
  x := 7;
  writeln(getX());
end.
