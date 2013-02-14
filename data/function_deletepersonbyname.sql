--Function: public.deletepersonbyname(Input firstname varchar, Input lastname varchar, Output rowsaffected integer)
--DROP FUNCTION public.deletepersonbyname(IN firstname varchar, IN lastname varchar, OUT rowsaffected integer);
CREATE OR REPLACE FUNCTION public.deletepersonbyname(IN firstname varchar, IN lastname varchar, OUT rowsaffected integer)
AS
$$
DECLARE
  admin_id INTEGER;
  rows_affected INTEGER;
  personid bigint;
  personrow person%ROWTYPE;
  personidrow person_identifier%ROWTYPE;
  dateVoided DATE;
BEGIN
  SELECT au.id FROM app_user au INTO admin_id WHERE au.username = 'admin';
  SELECT CURRENT_DATE INTO dateVoided;
  rowsaffected := 0;
  RAISE NOTICE 'Setting voided by to % and dateVoided to %', admin_id, dateVoided;
  RAISE NOTICE 'Looking for person named "%" "%"', firstname, lastname;
  FOR personrow IN
    SELECT * FROM person WHERE given_name = firstname AND family_name = lastname AND date_voided IS NULL
  LOOP
    personid := personrow.person_id;
    RAISE NOTICE 'Found person row named % %, will delete records with person_id %', personrow.given_name, personrow.family_name, personid;
    UPDATE person_identifier SET date_voided = dateVoided, voided_by_id = admin_id WHERE person_id = personid;
    UPDATE person SET date_voided = dateVoided, voided_by_id = admin_id WHERE person_id = personid;
    GET DIAGNOSTICS rows_affected = ROW_COUNT;
    rowsaffected := rowsaffected + rows_affected;
  END LOOP;
  RAISE NOTICE 'Update of person affected % rows', rowsaffected;
END
$$
LANGUAGE 'plpgsql'
VOLATILE
CALLED ON NULL INPUT
SECURITY INVOKER
COST 100;

ALTER FUNCTION public.deletepersonbyname(IN firstname varchar, IN lastname varchar, OUT rowsaffected integer)
  OWNER TO openempi;
