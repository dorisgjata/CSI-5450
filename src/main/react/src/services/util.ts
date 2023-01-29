import images_urls from "../data/images_urls";
export const parse = async () => {
    const response: any = await fetch("https://530828c83afb4338b9927d95f5792ed5.us-east-1.aws.found.io:9243/cmoa_objects/_msearch", {
        "credentials": "include",
        "headers": {
            "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/109.0",
            "Accept": "application/json",
            "Accept-Language": "en-US,en;q=0.5",
            "authorization": "Basic Y29sbGVjdGlvbnM6bzQxS0chTW1KUSRBNjY=",
            "content-type": "application/x-ndjson",
            "Sec-Fetch-Dest": "empty",
            "Sec-Fetch-Mode": "cors",
            "Sec-Fetch-Site": "cross-site"
        },
        "referrer": "https://collection.cmoa.org/",
        "body": "{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"match_all\":{}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516402,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"aggs\":{\"uniqueClassification\":{\"terms\":{\"field\":\"medium\",\"order\":{\"_key\":\"asc\"},\"shard_size\":2000,\"size\":500}},\"uniqueDepartment\":{\"terms\":{\"field\":\"department\",\"order\":{\"_key\":\"asc\"},\"shard_size\":2000,\"size\":500}},\"uniqueLocation\":{\"terms\":{\"field\":\"current_location\",\"order\":{\"_key\":\"asc\"},\"shard_size\":2000,\"size\":500}},\"creators\":{\"nested\":{\"path\":\"creators\"},\"aggs\":{\"uniqueCreator\":{\"terms\":{\"field\":\"creators.label\",\"order\":{\"_count\":\"desc\"},\"shard_size\":2000,\"size\":500},\"aggs\":{\"cited\":{\"terms\":{\"field\":\"creators.cited_name\"},\"aggs\":{\"sort\":{\"terms\":{\"field\":\"creators.cited_name.sort\"}}}}}},\"uniqueNationality\":{\"terms\":{\"field\":\"creators.nationality\",\"order\":{\"_key\":\"asc\"},\"shard_size\":2000,\"size\":500}}}}},\"sort\":[{\"_score\":\"desc\"}],\"size\":10,\"from\":0}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Fine Arts\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":2}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Fine Arts: Teenie Harris Archive\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":2}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Decorative Arts and Design\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":2}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Film and Video\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":1}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Heinz Architectural Center\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":1}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Modern and Contemporary Art\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":1}\n{}\n{\"_source\":[\"id\",\"title\",\"creators\",\"creation_date\",\"images\",\"type\"],\"query\":{\"function_score\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"department\":\"Photography\"}}],\"filter\":[{\"bool\":{\"must\":[{\"term\":{\"images.permitted\":true}},{\"exists\":{\"field\":\"images.filename\"}}]}}]}},\"random_score\":{\"seed\":1675014516403,\"field\":\"_seq_no\"},\"boost_mode\":\"replace\"}},\"from\":0,\"size\":1}\n",
        "method": "POST",
        "mode": "cors"
    });;
    if (!response.ok) {
        throw new Error("Couldn't get source");
    }
    response.json().then((data: any) => {
        console.log('Data', data);

        const artworks = data.hits.hits.map((hit: any) => {
            let sources: any = {};
            const artWorkId = extractId(hit._id);
            if (hit._source.images.length > 0) {
                hit._source.images.map((image: any, index: number) => {
                    let { irn, filename } = image;
                    let src = `https://cmoa-collection-images.s3.amazonaws.com/thing/${irnPath(irn)}/${filename}`;
                    sources[`src_${index}`] = src;
                });
            }
            else {
                console.log("Images not found");
            }
            return {
                artWorkId,
                ...sources
            }
        });
        const json = JSON.stringify(artworks);
        download(json);
    });
};
function download(content: any) {
    var a = document.createElement("a");
    var file = new Blob([content], { type: 'text/plain' });
    a.href = URL.createObjectURL(file);
    a.download = 'ids.json';
    a.click();
}

function extractId(str: string) {
    return str.split('/')[1] || '';
}

function irnPath(path: number) {
    return path.toString().padStart(4, '0');
}
