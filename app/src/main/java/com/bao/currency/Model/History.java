package com.bao.currency.Model;

public class History {
    private String _id;
    private String _result;
    private String _date;

    public History(String _id, String _result, String _date) {
        this._id = _id;
        this._result = _result;
        this._date = _date;
    }

    public History() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_result() {
        return _result;
    }

    public void set_result(String _result) {
        this._result = _result;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }
}
