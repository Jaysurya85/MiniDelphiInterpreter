var
  value: integer;

function echo(value);
begin
  return value;
end;

begin
  value := 7;
  writeln(echo(21));
  writeln(value);
end.
