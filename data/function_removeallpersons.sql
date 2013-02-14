--Function: public.removeallpersons(Output rowsaffected integer)
--DROP FUNCTION public.removeallpersons(OUT rowsaffected integer);

CREATE OR REPLACE FUNCTION public.removeallpersons(OUT rowsaffected integer)
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
    SELECT * FROM person
  LOOP
    personid := personrow.person_id;
    RAISE NOTICE 'Found person row named % %, will delete records with person_id %', personrow.given_name, personrow.family_name, personid;
    DELETE FROM person_identifier WHERE person_id = personid;
    DELETE FROM audit_event WHERE ref_person_id = personid OR alt_ref_person_id = personid;
    DELETE FROM person_link WHERE rh_person_id = personid OR lh_person_id = personid;
    DELETE FROM person WHERE person_id = personid;
    GET DIAGNOSTICS rows_affected = ROW_COUNT;
    rowsaffected := rowsaffected + rows_affected;
  END LOOP;
  RAISE NOTICE 'Remove of persons affected % rows', rowsaffected;
END
$$
LANGUAGE 'plpgsql'
VOLATILE
CALLED ON NULL INPUT
SECURITY INVOKER
COST 100;

ALTER FUNCTION public.removeallpersons(OUT rowsaffected integer)
  OWNER TO openempi;
