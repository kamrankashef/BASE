var API = {
    API_ROOT: "/api",
    DATA_FORMAT: 'json',
    generic_post: function (end_point, params, callback) {
        params['extra_data'] = {
            'cache_buster': Math.floor(Math.random() * 999999999)
        };

        $.post(API.API_ROOT + '/' + end_point,
            params,
            callback,
            API.DATA_FORMAT);
    }
    ,get_captcha: function (image_tag) {
        var rand_int = Math.floor(Math.random() * 1000000);
        image_tag.attr('src', API.API_ROOT + '/get_captcha?blah=' + rand_int);
        image_tag.focus();
    }
    ,)get_cap_friendly_player: function (cap_friendly_player_uuid, callback) {
        var params = {
            cap_friendly_player_uuid: cap_friendly_player_uuid
        };
        API.generic_post('get_cap_friendly_player', params, callback);
    }
    ,)delete_cap_friendly_player: function (cap_friendly_player_uuid, callback) {
        var params = {
            cap_friendly_player_uuid: cap_friendly_player_uuid
        };
        API.generic_post('delete_cap_friendly_player', params, callback);
    }
    ,)create_cap_friendly_player: function (age, birth_date_str, draft_rount, draft_year, drafted_by, drafted_overall, elc_signing_age, elite_prospect_id, full_name, height_str, number, shoots, team_name, waivers_signing_age, callback) {
            var params = {
                age: age,
                birth_date_str: birth_date_str,
                draft_rount: draft_rount,
                draft_year: draft_year,
                drafted_by: drafted_by,
                drafted_overall: drafted_overall,
                elc_signing_age: elc_signing_age,
                elite_prospect_id: elite_prospect_id,
                full_name: full_name,
                height_str: height_str,
                number: number,
                shoots: shoots,
                team_name: team_name,
                waivers_signing_age: waivers_signing_age
};

        API.generic_post('create_cap_friendly_player', params, callback);
    }
    ,)update_cap_friendly_player: function (cap_friendly_player_uuid, age, birth_date_str, draft_rount, draft_year, drafted_by, drafted_overall, elc_signing_age, elite_prospect_id, full_name, height_str, number, shoots, team_name, waivers_signing_age, callback) {
            var params = {
                cap_friendly_player_uuid: cap_friendly_player_uuid,
                age: age,
                birth_date_str: birth_date_str,
                draft_rount: draft_rount,
                draft_year: draft_year,
                drafted_by: drafted_by,
                drafted_overall: drafted_overall,
                elc_signing_age: elc_signing_age,
                elite_prospect_id: elite_prospect_id,
                full_name: full_name,
                height_str: height_str,
                number: number,
                shoots: shoots,
                team_name: team_name,
                waivers_signing_age: waivers_signing_age
            };

        API.generic_post('update_cap_friendly_player', params, callback);
    }
    ,)get_cap_friendly_contract: function (cap_friendly_contract_uuid, callback) {
        var params = {
            cap_friendly_contract_uuid: cap_friendly_contract_uuid
        };
        API.generic_post('get_cap_friendly_contract', params, callback);
    }
    ,)delete_cap_friendly_contract: function (cap_friendly_contract_uuid, callback) {
        var params = {
            cap_friendly_contract_uuid: cap_friendly_contract_uuid
        };
        API.generic_post('delete_cap_friendly_contract', params, callback);
    }
    ,)create_cap_friendly_contract: function (cap_friendly_player_guid, ch, expiry_status, length_str, signing_date, signing_team, type, value, callback) {
            var params = {
                cap_friendly_player_guid: cap_friendly_player_guid,
                ch: ch,
                expiry_status: expiry_status,
                length_str: length_str,
                signing_date: signing_date,
                signing_team: signing_team,
                type: type,
                value: value
};

        API.generic_post('create_cap_friendly_contract', params, callback);
    }
    ,)update_cap_friendly_contract: function (cap_friendly_contract_uuid, cap_friendly_player_guid, ch, expiry_status, length_str, signing_date, signing_team, type, value, callback) {
            var params = {
                cap_friendly_contract_uuid: cap_friendly_contract_uuid,
                cap_friendly_player_guid: cap_friendly_player_guid,
                ch: ch,
                expiry_status: expiry_status,
                length_str: length_str,
                signing_date: signing_date,
                signing_team: signing_team,
                type: type,
                value: value
            };

        API.generic_post('update_cap_friendly_contract', params, callback);
    }
    ,)get_cap_friendly_contract_year: function (cap_friendly_contract_year_uuid, callback) {
        var params = {
            cap_friendly_contract_year_uuid: cap_friendly_contract_year_uuid
        };
        API.generic_post('get_cap_friendly_contract_year', params, callback);
    }
    ,)delete_cap_friendly_contract_year: function (cap_friendly_contract_year_uuid, callback) {
        var params = {
            cap_friendly_contract_year_uuid: cap_friendly_contract_year_uuid
        };
        API.generic_post('delete_cap_friendly_contract_year', params, callback);
    }
    ,)create_cap_friendly_contract_year: function (cap_friendly_contract_guid, ahl_salary, avv, cap_hit, clause, nhl_salary, p_bonus, s_bonus, season, callback) {
            var params = {
                cap_friendly_contract_guid: cap_friendly_contract_guid,
                ahl_salary: ahl_salary,
                avv: avv,
                cap_hit: cap_hit,
                clause: clause,
                nhl_salary: nhl_salary,
                p_bonus: p_bonus,
                s_bonus: s_bonus,
                season: season
};

        API.generic_post('create_cap_friendly_contract_year', params, callback);
    }
    ,)update_cap_friendly_contract_year: function (cap_friendly_contract_year_uuid, cap_friendly_contract_guid, ahl_salary, avv, cap_hit, clause, nhl_salary, p_bonus, s_bonus, season, callback) {
            var params = {
                cap_friendly_contract_year_uuid: cap_friendly_contract_year_uuid,
                cap_friendly_contract_guid: cap_friendly_contract_guid,
                ahl_salary: ahl_salary,
                avv: avv,
                cap_hit: cap_hit,
                clause: clause,
                nhl_salary: nhl_salary,
                p_bonus: p_bonus,
                s_bonus: s_bonus,
                season: season
            };

        API.generic_post('update_cap_friendly_contract_year', params, callback);
    }
    ,)get_cap_friendly_stat_by_year: function (cap_friendly_stat_by_year_uuid, callback) {
        var params = {
            cap_friendly_stat_by_year_uuid: cap_friendly_stat_by_year_uuid
        };
        API.generic_post('get_cap_friendly_stat_by_year', params, callback);
    }
    ,)delete_cap_friendly_stat_by_year: function (cap_friendly_stat_by_year_uuid, callback) {
        var params = {
            cap_friendly_stat_by_year_uuid: cap_friendly_stat_by_year_uuid
        };
        API.generic_post('delete_cap_friendly_stat_by_year', params, callback);
    }
    ,)create_cap_friendly_stat_by_year: function (cap_friendly_player_guid, a, g, gp, league, pim, playoffs, playoffs_a, playoffs_g, playoffs_gp, playoffs_pim, playoffs_tp, season, team, tp, callback) {
            var params = {
                cap_friendly_player_guid: cap_friendly_player_guid,
                a: a,
                g: g,
                gp: gp,
                league: league,
                pim: pim,
                playoffs: playoffs,
                playoffs_a: playoffs_a,
                playoffs_g: playoffs_g,
                playoffs_gp: playoffs_gp,
                playoffs_pim: playoffs_pim,
                playoffs_tp: playoffs_tp,
                season: season,
                team: team,
                tp: tp
};

        API.generic_post('create_cap_friendly_stat_by_year', params, callback);
    }
    ,)update_cap_friendly_stat_by_year: function (cap_friendly_stat_by_year_uuid, cap_friendly_player_guid, a, g, gp, league, pim, playoffs, playoffs_a, playoffs_g, playoffs_gp, playoffs_pim, playoffs_tp, season, team, tp, callback) {
            var params = {
                cap_friendly_stat_by_year_uuid: cap_friendly_stat_by_year_uuid,
                cap_friendly_player_guid: cap_friendly_player_guid,
                a: a,
                g: g,
                gp: gp,
                league: league,
                pim: pim,
                playoffs: playoffs,
                playoffs_a: playoffs_a,
                playoffs_g: playoffs_g,
                playoffs_gp: playoffs_gp,
                playoffs_pim: playoffs_pim,
                playoffs_tp: playoffs_tp,
                season: season,
                team: team,
                tp: tp
            };

        API.generic_post('update_cap_friendly_stat_by_year', params, callback);
    }
};
