const {restSuccess, restServerError} = require("./utils");
const {selectById, selectAll, countAll, insert, updateById, deleteById, deleteByIds} = require("./dao");
const {v4: uuidv4} = require('uuid');

function buildSortBySql(sortBy) {
}

function getParams(params, columns) {
    return Object.entries(params).filter(entry => /*!entry[0].startsWith('page.') && !entry[0].startsWith('sortBy.') && entry[1] !== undefined && */entry[1] !== null && Object.prototype.hasOwnProperty.call(columns, entry[0]))
        .map(entry => {
            const column = columns[entry[0]];
            return {name: entry[0], value: entry[1], condition: column?.condition || 'equals', fn: column?.fn};
        });
}

function getSortBy(params, columns) {
    if (params['sortBy.name'] && Object.prototype.hasOwnProperty.call(columns, params['sortBy.name'])) {
        return {
            name: params['sortBy.name'],
            // explicitly specify the order literal to prevent sql injection
            order: params['sortBy.order'] === 'desc' ? 'DESC' : 'ASC',
        }
    }
    return null;
}

function useRoutes(entries, app) {
    for (const entry of entries) {
        const prefix = `${entry.prefix || ""}/${entry.name || entry.table}`;
        app.get(`${prefix}/read`, async (req, res) => {
            const id = req.query.id;
            try {
                const item = await selectById(entry.table, id);
                res.json(restSuccess(item));
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
        app.post(`${prefix}/create`, async (req, res) => {
            try {
                const params = getParams(req.body, entry.columns);

                params.push({name: 'createTime', value: new Date().getTime()});
                if (entry.table === 'user') {
                    params.push({name: 'password', value: '123456'});
                }
                // Generate value for primary key as it is not automatically generated.
                const id = uuidv4();
                params.push({name: 'id', value: id});
                await insert(entry.table, params);
                res.json(restSuccess(await selectById(entry.table, id)));
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
        app.post(`${prefix}/update`, async (req, res) => {
            try {
                const excludedColumns = ['id', 'createTime', 'updateTime', 'creatorId', 'lastModifierId', 'lastModifyTime'];
                const id = req.body.id;
                const params = getParams(req.body, entry.columns).filter(param => !excludedColumns.includes(param.name));

                await updateById(entry.table, id, params);
                res.json(restSuccess(await selectById(entry.table, id)));
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
        app.post(`${prefix}/delete`, async (req, res) => {
            try {
                const id = req.body.id;
                await deleteById(entry.table, id);
                res.json(restSuccess());
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
        app.post(`${prefix}/batchDelete`, async (req, res) => {
            try {
                const ids = req.body.ids.split(',').map(_ => _.trim()).filter(_ => _.length > 0);
                await deleteByIds(entry.table, ids);
                res.json(restSuccess());
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
        app.get(`${prefix}/list`, async (req, res) => {
            try {
                const params = getParams(req.query, entry.columns);
                const sortBy = getSortBy(req.query);
                const items = await selectAll(entry.table, params, sortBy);
                restSuccess(items);
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
        app.get(`${prefix}/paginate`, async (req, res) => {
            const page = parseInt(req.query["page.page"] || "1");
            const size = parseInt(req.query["page.size"] || "10");
            const offset = (page - 1) * size;
            const params = getParams(req.query, entry.columns);
            const sortBy = getSortBy(req.query, entry.columns);
            try {
                const count = await countAll(entry.table, params);
                const totalPages = Math.ceil(count / size);
                const items = await selectAll(entry.table, params, sortBy, {
                    limit: size,
                    offset,
                });
                res.json(
                    restSuccess({
                        page,
                        size,
                        totalPages,
                        totalCount: count,
                        items,
                    })
                );
            } catch (err) {
                console.error(err);
                res.json(restServerError(String(err)));
            }
        });
    }
}

module.exports = useRoutes;
