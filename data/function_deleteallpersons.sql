--Function: public.deleteallpersons(Output rowsaffected integer)

--DROP FUNCTION public.deleteallpersons(OUT rowsaffected integer);

CREATE OR REPLACE FUNCTION public.deleteallpersons(OUT rowsaffected integer)
AS
$$
DECLARE
  admin_id INTEGER;
  rows_affected INTEGER;
  personid bigint;
  personrow person%ROWTYPE;
  dateVoided DATE;
BEGIN
  rowsaffected := 0;
  SELECT au.id FROM app_user au INTO admin_id WHERE au.username = 'admin';
  SELECT CURRENT_DATE INTO dateVoided;
  RAISE NOTICE 'Setting voided by to % and dateVoided to %', admin_id, dateVoided;
  FOR personrow IN
    SELECT * FROM person WHERE date_voided IS NULL
  LOOP
    personid := personrow.person_id;
    RAISE NOTICE 'Found person row named % %, will delete records with person_id %', personrow.given_name, personrow.family_name, personid;
    UPDATE person_identifier SET date_voided = dateVoided, voided_by_id = admin_id WHERE person_id = personid;
    GET DIAGNOSTICS rows_affected = ROW_COUNT;
    RAISE NOTICE 'Update of person identifier affected % rows', rows_affected;
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

ALTER FUNCTION public.deleteallpersons(OUT rowsaffected integer)
  OWNER TO openempi;
