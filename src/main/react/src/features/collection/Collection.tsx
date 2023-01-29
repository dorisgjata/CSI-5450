
import { useCallback, useEffect, useState } from "react";
import { Grid, Typography, CircularProgress, IconButton, Box } from "@mui/material";
import { Artwork } from './Artwork';
import { useGetCollectionQuery, usePrefetch } from '../../services/api';
import { Artwork as ArtworkType } from "../../app/types";
import { useAppSelector } from "../../app/hooks";
import { Redirect } from "react-router-dom";
import { Masonry } from "@mui/lab";
import { ArrowDownward } from "@mui/icons-material";

export function Collection() {
  const { isLoggedIn } = useAppSelector((state: { auth: any; }) => state.auth);
  const totalPages = 1200;
  const limit = 50;
  const [offset, setOffset] = useState(0);

  const [page, setPage] = useState(1);
  const { data, isError, isFetching, isLoading } = useGetCollectionQuery(offset);
  const prefetchOffset = usePrefetch('getCollection');
  const [collection, setCollection] = useState<ArtworkType[] | undefined>(data || []);

  useEffect(() => {
    setCollection(data);
  },);
  const loadMore = () => {
    if (page < totalPages) {
      setPage(page + 1);
      setOffset((page) * limit);
      if (data && collection) {
        setCollection([...collection, ...data]);
      }
    }

  }
  const prefetchNext = useCallback(() => {
    prefetchOffset(offset)
  }, [prefetchOffset, offset])

  if (!isLoggedIn) {
    return <Redirect to='/login' />;
  }
  if (isLoading || isFetching) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center' }}>
        <CircularProgress />
      </Box>);
  }
  if (!data?.length || isError) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        An error occurred.
      </Box>);
  }


  return (
    <Box>
      <Typography sx={{ p: 2 }}>
        Carnegie Museum of Art is arguably the first museum of contemporary art in the United States, collecting the “Old Masters of tomorrow” since the inception of the Carnegie International in 1896.
        Our collection of more than 34,000 objects features a broad spectrum of visual art, including painting and sculpture; prints and drawings; photographs; architectural casts, renderings, and models; decorative arts and design; and film, video, and digital imagery. The museum also houses the archive of over 70,000 negatives by photographer Charles “Teenie” Harris.
      </Typography>

      <Grid container spacing={1} sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: "column" }}>

        <Masonry columns={{ xs: 2, sm: 3, md: 4, lg: 6 }} spacing={2}>
          {collection ? collection.map((artwork: ArtworkType, index: any) => {
            return (
              <Box key={artwork.artworkId} >
                <Artwork key={artwork.artworkId} artwork={artwork} />
              </Box>)
          }) : <Box src="https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png" component="img" />}
        </Masonry>

        <Grid item sx={{ display: 'flex', alignSelf: 'center' }}>
          <IconButton onClick={loadMore} onMouseEnter={prefetchNext}>
            <ArrowDownward />
          </IconButton>
        </Grid>
      </Grid>
    </Box>
  )
}

