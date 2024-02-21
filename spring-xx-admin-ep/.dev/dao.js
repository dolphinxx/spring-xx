const {db} = require('./db');
const {restSuccess} = require("./utils");

function escapeSql(str) {
    // TODO:
    return str;
}

function buildWhereClause(params) {
    if (!params || params.length === 0) {
        return null;
    }
    const searchFragments = [];
    const searchParams = [];
    for (const param of params) {
        let column = `\`${param.name}\``;
        let placeholder = '?'
        if (param.fn) {
            if (param.fn === 'DATE') {
                column = `STRFTIME('%Y-%m-%d', DATETIME(${column}/1000, 'unixepoch'), 'localtime')`;
            } else if (params.fn === 'DATETIME') {
                column = `STRFTIME('%Y-%m-%d %H:%M:%S', DATETIME(${column}/1000, 'unixepoch'), 'localtime')`;
            } else {
                column = `${param.fn}(${column})`;
            }

        }
        if (param.condition === 'isNull') {
            searchFragments.push(`${column} IS NULL`);
            continue;
        }
        if (param.condition === 'isNotNull') {
            searchFragments.push(`${column} IS NOT NULL`);
            continue;
        }
        let value = param.value;
        let logic;
        switch (param.condition) {
            case 'contains':
                logic = `LIKE`;
                value = '%' + value + '%';
                break;
            case 'startsWith':
                logic = `LIKE`;
                value = value + '%';
                break;
            case 'endsWith':
                logic = `LIKE`;
                value = '%' + value;
                break;
            case 'notEquals':
                logic = `<>`;
                break;
            default:
                logic = '=';
        }
        searchFragments.push(`${column} ${logic} ${placeholder}`);
        searchParams.push(value);
    }
    return {
        sql: ' WHERE ' + searchFragments.join(' AND '),
        params: searchParams,
    };
}

async function selectById(table, id) {
    return new Promise((resolve, reject) => {
        db.get(
            `SELECT * FROM \`${table}\` WHERE id = ?`,
            [id],
            (err, row) => {
                if (err) {
                    reject(err);
                    return;
                }
                resolve(row);
            }
        );
    });
}

async function selectAll(table, params, sortBy, paginate) {
    // NOTICE: should filter column names in prod to prevent sql injection
    const whereClause = buildWhereClause(params);
    const offsetSql = paginate ? ` LIMIT ${paginate.limit} OFFSET ${paginate.offset}` : '';
    const sortBySql = sortBy ? ` ORDER BY \`${sortBy.name}\` ${sortBy.order}` : '';
    console.log(whereClause, sortBySql, offsetSql);
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(`SELECT * FROM \`${table}\`${whereClause ? whereClause.sql : ''}${sortBySql}${offsetSql}`, whereClause ? whereClause.params : [], err => {
            if (err) {
                reject(err);
            }
        });
        if (!stmt) {
            return;
        }
        stmt.all((err, rows) => {
            if (err) {
                reject(err);
                return;
            }
            resolve(rows);
        });
        // db.all(`SELECT * FROM \`${table}\`${offsetSql}`, (err, rows) => {
        //   if (err) {
        //     reject(err);
        //     return;
        //   }
        //   resolve(rows);
        // });
    });
}

async function countAll(table, params) {
    const whereClause = buildWhereClause(params);
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(`SELECT COUNT(0) AS total FROM \`${table}\`${whereClause ? whereClause.sql : ''}`, whereClause ? whereClause.params : [], err => {
            if (err) {
                reject(err);
            }
        });
        if (!stmt) {
            return;
        }
        stmt.get((err, row) => {
            if (err) {
                reject(err);
                return;
            }
            resolve(row.total);
        });
    });
}

async function insert(table, params) {
    if (!params || params.length === 0) {
        throw new Error('columns is empty');
    }
    const columns = [];
    const values = [];
    for (const param of params) {
        columns.push(param.name);
        values.push(param.value);
    }
    const columnsSql = '`' + columns.join('`, `') + '`';
    const placeholdersSql = columns.map(_ => '?').join(', ');
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(`INSERT INTO \`${table}\`(${columnsSql}) VALUES (${placeholdersSql})`, err => {
            if (err) {
                reject(err);
            }
        });
        if (!stmt) {
            return;
        }
        stmt.run(values, function (err) {
            if (err) {
                reject(err);
                return;
            }
            resolve(this.lastID);
        });
    })
}

async function updateById(table, id, params) {
    if (!params || params.length === 0) {
        throw new Error('columns is empty');
    }
    if (!id) {
        throw new Error('id is missing');
    }
    const columns = [];
    const values = [];
    for (const param of params) {
        columns.push(param.name);
        values.push(param.value);
    }
    values.push(id);
    const columnsSql = columns.map(c => `\`${c}\` = ?`).join(', ');
    return new Promise((resolve, reject) => {
        const sql = `UPDATE \`${table}\` SET ${columnsSql} WHERE id = ?`;
        console.log(sql);
        const stmt = db.prepare(sql, err => {
            if (err) {
                reject(err);
            }
        });
        if (!stmt) {
            return;
        }
        stmt.run(values, function (err) {
            if (err) {
                reject(err);
                return;
            }
            resolve(this.changes);
        })
    })
}

async function deleteById(table, id) {
    if (!id) {
        throw new Error('id is missing');
    }
    return new Promise((resolve, reject) => {
        db.run(`DELETE FROM \`${table}\` WHERE id = ?`, [id], function (err) {
            if (err) {
                reject(err);
                return;
            }
            resolve(this.changes);
        });
    })
}

async function deleteByIds(table, ids) {
    if (!ids || ids.length === 0) {
        throw new Error('ids is missing or empty');
    }
    return new Promise((resolve, reject) => {
        const placeholders = ids.map(() => '?').join(', ');
        const sql = `DELETE FROM \`${table}\` WHERE id IN(${placeholders})`;
        const stmt = db.prepare(sql, err => {
            if (err) {
                reject(err);
            }
        });
        if (!stmt) {
            return;
        }
        stmt.run(ids, function (err) {
            if (err) {
                reject(err);
                return;
            }
            resolve(this.changes);
        })
    });
}

module.exports = {
    selectById,
    selectAll,
    countAll,
    insert,
    updateById,
    deleteById,
    deleteByIds,
}
