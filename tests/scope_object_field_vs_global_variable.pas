class Holder
begin
  var value: integer;

  constructor Create();
  begin
    value := 5;
  end;

  procedure show();
  begin
    writeln(value);
  end;
end;

var
  value: integer;
  h: Holder;

begin
  value := 99;
  h := Holder.Create();
  h.show();
  writeln(value);
end.
