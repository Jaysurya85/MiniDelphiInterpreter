procedure show(x);
begin
  writeln(x);
end;

function getVal(x);
begin
  return x;
end;

begin
  show(10);
  writeln(getVal(5));
end.
