import images_urls from "../data/images_urls";

export const getImage = async (artworkId: string) => {
    const response: any = await fetch(`https://530828c83afb4338b9927d95f5792ed5.us-east-1.aws.found.io:9243/cmoa_objects/object/cmoa%3Aobjects%2F${artworkId}/_source`, {
        "headers": {
            "accept": "*/*",
            "accept-language": "en-US,en;q=0.9",
            "authorization": "Basic Y29sbGVjdGlvbnM6bzQxS0chTW1KUSRBNjY=",
            "sec-fetch-dest": "empty",
            "sec-fetch-mode": "cors",
            "sec-fetch-site": "cross-site",
            "sec-gpc": "1",
            "Referer": "https://collection.cmoa.org/",
        },
        "body": null,
        "method": "GET"
    });

    response.json().then((data: any) => {
        console.log('Data', data);
        if (!response.ok) {
            throw new Error("Couldn't get source");
        }
        console.log(data);

        const { irn, filename } = data.images[0];
        const irnPath = irn.toString().padStart(4, '0');
        const src = `https://cmoa-collection-images.s3.amazonaws.com/thing/${irnPath}/${filename}`;
        console.log(src);
        return src;
    });
};
export const getImageSrc = (allUrls: string[]) => {
    let resolvedUrl = '';
    let fetchPromises = [];
    console.log(allUrls.length);
    for (let url of allUrls) {
        console.log("image.ts url", url);

        let response: any = fetch(url, {
            "credentials": "omit",
            "headers": {
                "Host": "cmoa-collection-images.s3.amazonaws.com",
                "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/109.0",
                "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                "Accept-Language": "en-US,en;q=0.5",
                "Upgrade-Insecure-Requests": "1",
                "Sec-Fetch-Dest": "document",
                "Sec-Fetch-Mode": "navigate",
                "Sec-Fetch-Site": "none",
                "Sec-Fetch-User": "?1",
                "Access-Control-Allow-Origin": "*",
                "Origin": "https://collection.cmoa.org/"
            },
            "referrer": "https://collection.cmoa.org/",
            "method": "GET",
            "mode": "cors"
        });
        fetchPromises.push(response);
        Promise.all(fetchPromises)
            .then((responses) => {
                for (const response of responses) {
                    console.log(`${response.url}: ${response.status}`);
                }
            })
            .catch((error) => {
                console.error(`Failed to fetch: ${error}`)
            });
    };
};

